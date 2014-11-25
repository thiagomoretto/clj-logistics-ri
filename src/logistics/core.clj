(ns logistics.core
  (:require [liberator.core :refer [resource defresource]]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer [defroutes ANY GET]]
            [loom.graph :refer [weighted-graph]]
            [clojure.data.json :refer [write-str read-str]]
            [logistics.business :as biz])
  (:gen-class))


; Sample graph
; A B 10
; B D 15
; A C 20
; C D 30
; B E 50
; D E 30
(def wg (weighted-graph
  {:a {:b 10 :c 20} :b { :d 15 :e 50 } :c {:d 30} :d {:e 30}}))

; Resources
(def resource-defaults { :available-media-types ["application/json"] })

(defresource shortest-path-resource [origin target & [value]] resource-defaults
  :handle-ok (fn [_] (let [a (keyword origin)
                           z (keyword target)
                           v (if value (keyword value) identity)]
                      (write-str
                        (v (biz/shortest-path wg a z))))))

; Routes
(defroutes app
  (GET "/shortest-path/:origin/:target" [origin, target] (shortest-path-resource origin target))
  (GET "/shortest-path/:origin/:target/:value" [origin, target, value] (shortest-path-resource origin target value))
  (GET "/check" [] "OK"))

(def handler
  (-> app
      wrap-params))
