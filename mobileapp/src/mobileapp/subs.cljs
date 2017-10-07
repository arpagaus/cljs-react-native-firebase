(ns mobileapp.subs
  (:require [re-frame.core :refer [reg-sub]]))


(reg-sub
 :get-greeting
 (fn [db _]
   (:greeting db)))

(reg-sub
 :get-category-aggregates
 (fn [db _]
   (:category-aggregates db)))

(reg-sub
 :get-purchases
 (fn [db _]
   (:purchases db)))
