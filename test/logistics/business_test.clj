(ns logistics.business-test
  (:require [clojure.test :refer :all]
            [loom.graph :refer [weighted-graph]]
            [logistics.business :refer :all]))

(def sample-mesh (weighted-graph
  {:a {:b 10 :c 20} :b { :d 15 :e 50 } :c {:d 30} :d {:e 30}}))

(defn setup-case [mesh origin target expected-cost expected-path]
  (let [result (shortest-path mesh origin target)]
    (testing (str "The cost should be " expected-cost "."
      (is (= expected-cost (:cost result))))
    (testing "The path should be " expected-path "."
      (is (= expected-path (:path result)))))))

(deftest test-shortest-path-from-a-to-d
  (setup-case sample-mesh :a :d 25 ["a" "b" "d"]))

(deftest test-shortest-path-from-a-to-e
  (setup-case sample-mesh :a :e 55 ["a" "b" "d" "e"]))





