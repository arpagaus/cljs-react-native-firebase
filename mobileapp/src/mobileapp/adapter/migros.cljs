(ns mobileapp.adapter.migros
  (:require [happy.core :as happy :refer [GET PUT]]
            [happy.client.okhttp :as happy.client]))

(happy/set-default-client! (happy.client/create))

(GET "https://api.github.com/users" {} {:handler #(println "received " %)})
