(ns mcumuluz-fetcher.category
  (:require [clojure.string :as s]
            [java-time :as time]
            [cheshire.core :as json]
            [com.ashafa.clutch :as clutch]))

(def response-dir (clojure.java.io/file "/home/remo/migros-products"))

(defn parse-json-file [f]
  (try
    (json/parse-stream (clojure.java.io/reader f) true)
    (catch java.io.IOException e (.printStackTrace e))))

(->
  (parse-json-file "/home/remo/migros-products/lena+kleinfahrzeuge.json"))


(defn extract-category [article-response]
  (->> article-response
    (map #(update % :categories first))
    (map (fn [x] [(:name x) (get-in x [:categories :name])]))
    first))

#_(run! #(let [res (extract-category (parse-json-file %))]
            (println (.getName %) ";" (get res 0) ";" (get res 1)))
    (sort-by #(.getName %) (.listFiles response-dir)))

(run! #(->> (parse-json-file %)
         (map (fn [m] (assoc m :_id (:id m))))
         (clutch/bulk-update "mig-product"))
  (.listFiles response-dir))

(clutch/get-database "mig-product")

(clutch/put-document "clutch_example" {:test-grade 10} {:id "foo"})

(clutch/bulk-update "clutch_example" [{:test-grade 10 :_id "foo"}
                                      {:test-grade 20}
                                      {:test-grade 30}])
