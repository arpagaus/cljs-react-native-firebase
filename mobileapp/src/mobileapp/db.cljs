(ns mobileapp.db
  (:require [clojure.spec.alpha :as s]
            [cljs-time.core :as t]))


(defn color? [color] (re-matches #"#[0-9a-fA-F]{3,6}" color))

(s/def ::id int?)
(s/def ::text string?)
(s/def ::value number?)
(s/def ::color color?)
(s/def ::icon string?)

(s/def ::instant string?)
(s/def ::location string?)

(s/def ::turnover number?)
(s/def ::quantity number?)

(s/def ::purchase-article
  (s/keys :req-un [::text ::quantity ::turnover]))
(s/def ::articles
  (s/coll-of ::purchase-article))

(s/def ::purchase
  (s/keys :req-un [#_::instant ::location ::turnover]
          :opt-un [::articles]))
(s/def ::purchases
  (s/coll-of ::purchase))

(s/def ::aggregate
  (s/keys :req-un [::id ::text ::value]
          :opt-un [::color ::icon]))
(s/def ::category-aggregates
  (s/coll-of ::aggregate))

(s/def ::app-db
  (s/keys :opt-un [::category-aggregates ::purchases]))

;; initial state of app-db
(def app-db {:category-aggregates
             [{:id 1 :text "Fleisch" :value 32.5 :color "#D32F2F"}
              {:id 2 :text "Milchprodukte" :value 24.8 :color "#FBC02D"}
              {:id 3 :text "Früchte" :value 18.45 :color "#512DA8"}
              {:id 4 :text "Gemüse" :value 12.8 :color "#388E3C"}
              {:id 5 :text "Andere" :value 11.2 :color "#607D8B"}]
             :purchases
             [{:instant "Gestern, 10:23" :location "M Urdorf" :turnover 55
               :articles [{:text "Bio Limonen Rauchlachs" :quantity 1 :turnover 11.4}
                          {:text "Pouletschnitzel 2 Stk." :quantity 0.273 :turnover 7.65}
                          {:text "Bio Apfelringe" :quantity 1 :turnover 6.5}
                          {:text "HM Classic Pulver" :quantity 1 :turnover 5.4}
                          {:text "Greyerzer Plättli" :quantity 0.07 :turnover 5.3}
                          {:text "Bifidus Nature" :quantity 2 :turnover 1.3}]}
              {:instant "Gestern, 10:23" :location "MM Shoppi Spreitenbach" :turnover 55
               :articles [{:text "Mehrwegtasche" :quantity 1 :turnover 2}
                          {:text "Blévita Tom.-Basilikum" :quantity 1 :turnover 3.55}
                          {:text "Alnatura Tee-Auswahl" :quantity 1 :turnover 3.2}
                          {:text "Nusskäse" :quantity 0.209 :turnover 4.6}]}]})


#_(do (println (js/Date.))
      (s/explain ::app-db app-db))
