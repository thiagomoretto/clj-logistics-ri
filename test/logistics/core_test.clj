(ns logistics.core-test
  (:require [clojure.test :refer :all]
            [loom.graph :refer [weighted-graph]]
            [clojure.data.json :refer [write-str]]
            [logistics.core :refer :all]
            [logistics.redis :as db]))

; Fixtures and utilities
(def test-mesh (weighted-graph
  {:a {:b 15 :c 20} :b { :d 20 :e 50 } :c {:d 30} :d {:e 30}}))

(defn request [resource web-app & params]
  (web-app {:request-method :get :uri resource :params (first params)}))

(defn with-mesh [mesh fun]
  (with-redefs [db/pget (fn [k] mesh) db/psave (fn [k v] nil)]
    (fun)))

; Tests
(deftest test-check-route
  (testing "If the check route respond the OK message"
    (is (= 200 (:status (request "/check" app))))
    (is (= "OK" (:body (request "/check" app))))))

(deftest test-shortest-path-route-from-a-to-d
  (with-mesh test-mesh
    #(testing "If the shortest path returns the expected result"
      (is (= (write-str {:path ["a" "b" "d"] :cost 35}) (:body (request "/shortest-path/test-mock-mesh/a/d" app)))))))

(deftest test-shortest-cost-route-from-a-to-d
  (with-mesh test-mesh
    #(testing "If the shortest cost returns the expected result"
      (is (= (write-str 35) (:body (request "/shortest-path/test-mock-mesh/a/d/cost" app)))))))
