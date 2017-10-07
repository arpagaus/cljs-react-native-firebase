(ns mcumuluz-fetcher.parser
  (:require [clojure.string :as s]
            [java-time :as time]
            [clojure.edn :as edn])
  (:import [java.io BufferedReader StringReader]))

(defn parse-csv
  "Returns a sequence of vectors for a given CSV wrapped in a BufferedReader"
  [reader]
  (->>
   (line-seq reader)
   rest
   (map #(clojure.string/split % #";"))
   doall))

(defn coerce-datetime
  [date time]
  (time/local-date-time "dd.MM.yyyyHH:mm:ss" (str date time)))

(defn coerce-item
  [[_ _ _ _ _ article quantity sale turnover]]
  {:article article
   :quantity (edn/read-string quantity)
   :sale (edn/read-string sale)
   :turnover (edn/read-string turnover)})

(defn coerce-lines
  [csv]
  (->>
   csv
   (group-by #(take 5 %))
   (map (fn [[[date time store register trx] items]]
          {:datetime (coerce-datetime date time)
           :store store
           :register-no (edn/read-string register)
           :transaction-no (edn/read-string trx)
           :items (map coerce-item items)}))))

(coerce-lines
 (parse-csv (BufferedReader. (StringReader. (slurp "~/1.csv")))))

; Coop receipt
{:store "Coop Supermarkt Urdorf",
 :store-no "0004866"
 :purchase-no1 "06050"
 :purchase-no2 "00392406"
 :purchase-no3 "001"
 :datetime "2017-10-07T14:52:19",
 :items '({:article "Max Havelaar Banane offen",
           :quantity 0.932
           :original-price 3.8
           :sale-price 1.9
           :turnover 3.3,
           :tax-code "0"})}
