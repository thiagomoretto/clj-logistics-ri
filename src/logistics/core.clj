(ns logistics.core
  (:require [liberator.core :refer [resource defresource]]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer [defroutes ANY GET]]
            [loom.graph :refer [weighted-graph]]
            [clojure.data.json :refer [write-str read-str]]
            [logistics.business :as biz]
            [logistics.redis :as db])
  (:gen-class))


; TODO
; Sample graph
(def wg (weighted-graph
  {:a {:b 10 :c 20} :b { :d 15 :e 50 } :c {:d 30} :d {:e 30}}))

(db/psave "mesh1" wg)

; Resources
(def resource-defaults { :available-media-types ["application/json"] })

(defresource shortest-path-resource [mesh origin target & [value]] resource-defaults
  :exists? (fn [_]
             (let [m (db/pget mesh)]
               (if-not (nil? m)
                 {::entry
                  (let [a (keyword origin)
                        z (keyword target)
                        v (if value (keyword value) identity)]
                    (write-str
                     (v (biz/shortest-path m a z))))})))
  :handle-ok ::entry)

; Routes
(defroutes app
  (GET "/shortest-path/:mesh/:origin/:target" [mesh, origin, target] (shortest-path-resource mesh origin target))
  (GET "/shortest-path/:mesh/:origin/:target/:value" [mesh, origin, target, value] (shortest-path-resource mesh origin target value))
  (GET "/check" [] "OK"))

(def handler
  (-> app
      wrap-params))
