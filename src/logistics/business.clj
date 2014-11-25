(ns logistics.business
  (:require [loom.alg :refer [dijkstra-path dijkstra-path-dist]]))

(defn shortest-path [mesh origin target]
  (let [result (dijkstra-path-dist mesh origin target)]
    {:path (into [] (map (fn [x] (name x)) (first result)))
     :cost (last result)}))
