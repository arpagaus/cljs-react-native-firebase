(ns mcumuluz-fetcher.fetcher
  (:gen-class)
  (:require [clojure.string :as s]
            [clj-http.client :as client]
            [java-time :as time]
            [pl.danieljanus.tagsoup :as tagsoup]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [happy.core :as happy :refer [GET PUT]]
            [happy.client.okhttp :as happy.client]))

(happy/set-default-client! (happy.client/create))

(def ^:dynamic *session-id*)

(def purchase-id-url
  "https://www.migros.ch/de/meine-migros/cumulus/kassenbons/mit-cumulus/content/01/ajaxContent/0.html")

(def purchase-csv-url
  "https://www.migros.ch/service/avantaReceiptExport/csv.csv")

(def time-decrement (time/months 3))

(defn extract-checkboxes
  [response-body]
  (let [soup (tagsoup/parse-string response-body)
        csv-form (get-in soup [2 4 4])
        checkboxes (->> csv-form
                        (filter #(some? (get-in % [1 :name])))
                        (filter #(s/starts-with? (get-in % [1 :name]) "checkbox"))
                        (map (fn [m] (get-in m [1 :value]))))]
    checkboxes))

(defn list-purchase-ids
  ([from to]
   (loop [page 1
          ids '()]
     (let [new-ids (list-purchase-ids from to page)]
       (if (empty? new-ids)
         ids
         (recur (inc page) (concat ids new-ids))))))

  ([from to page]
   (let [period (str (time/format "YYYY-MM-dd" from)
                     "_"
                     (time/format "YYYY-MM-dd" to))
         response  (client/get purchase-id-url
                               {:query-params {:period period
                                               :p page}
                                :cookies {:JSESSIONID {:value *session-id*}}})]
     (extract-checkboxes (:body response)))))

(defn fetch-all-purchase-ids
  ([] (fetch-all-purchase-ids (time/local-date)))
  ([to-date]
   (loop [to to-date
          from (time/minus to-date time-decrement)
          ids '()]
     (println "from:" from "to:" to "ids:" (count ids))
     (let [more-ids (list-purchase-ids from to)]
       (if (empty? more-ids)
         ids
         (recur from
                (time/minus from time-decrement)
                (concat ids more-ids)))))))

(defn checkbox-param-names
  ([] (checkbox-param-names 1))
  ([n] (lazy-seq (cons (str "checkbox" n) (checkbox-param-names (inc n))))))

(defn get-ids-as-form-params [ids]
  (->> ids
       (partition-all 10)
       (map #(reduce-kv
              (fn [acc i id]
                (assoc acc (keyword (str "checkbox" (inc i))) id)) {} (into [] %)))))

(defn fetch-csv [ids]
  (let [counter (atom 0)]
    (->> (get-ids-as-form-params ids)
         (map #(assoc % :details   "true"))
         (map #(client/post purchase-csv-url
                            {:form-params %
                             :cookies {:JSESSIONID {:value *session-id*}}}))
         (map :body)
         (map #(do (spit (str (swap! counter inc) ".csv") %) %))
         (map clojure.string/split-lines)
         (map rest)
         flatten
         (map #(clojure.string/split % #";"))
         doall)))

#_(def all-ids
    (binding [*session-id* ""]
      (fetch-all-purchase-ids)))

#_(def all-items
    (binding [*session-id* ""]
      (fetch-csv all-ids)))

;; curl 'https://www.migros.ch/de/meine-migros/cumulus/kassenbons/mit-cumulus/content/01/ajaxContent/0.html?period=2015-10-01_2017-01-01&p=4' -H 'Cookie: JSESSIONID=B69757805CB040C289F7CFF3AF7ED394'
;; curl -X POST 'https://www.migros.ch/service/avantaReceiptExport/csv.csv' --data-binary 'sort=dateDsc&language=it&details=true&checkbox1=20160621_105735_0076600_3_1057'  -H 'Cookie: JSESSIONID=B69757805CB040C289F7CFF3AF7ED394'
