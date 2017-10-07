(ns mcumuluz-fetcher.analytics
  (:require [clojure.string :as s]))

(def all-items [])

(->> all-items
     (filter #(get % 5))
     (group-by #(get % 5))
     (map (fn [[k v]] [k (count v)]))
     (sort-by #(get % 1))
     reverse)

(->> all-items
     (filter #(get % 5))
     (group-by #(get % 5))
     (map (fn [[k v]] [k (count v)]))
     (sort-by #(get % 1))
     reverse
     (map #(get % 0))
     (map #(.toLowerCase %))
  ;(map #(ring.util.codec/url-encode %))
     (map #(clojure.string/replace % #"\s+" "+"))
     distinct)

(->> all-items
     (filter #(= "Bananen" (get % 5)))
     (group-by #(get % 5)))

(->> all-items
     (filter #(re-matches #"[0-9]+" (get % 6)))
     (group-by #(let [article (get % 5)
                      quantity (bigdec (get % 6))
                      turnover (bigdec (get % 8))
                      price (with-precision 10 (/ turnover quantity))]
                  [article price]))
     (map (fn [[k v]] [k (count v)]))
     (sort-by #(get % 1))
     reverse)

(->> all-items
     (map (fn [r] [(nth r 4) (take 4 r)]))
     (group-by first)
     (map (fn [[k v]] [k (into #{} v)]))
     (filter #(> (count (nth % 1)) 1)))
