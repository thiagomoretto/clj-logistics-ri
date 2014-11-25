(ns logistics.core-test
  (:require [clojure.test :refer :all]
            [clojure.data.json :refer [write-str]]
            [logistics.core :refer :all]))

(defn request [resource web-app & params]
  (web-app {:request-method :get :uri resource :params (first params)}))

(deftest test-check-route
  (testing "If the check route respond the OK message"
    (is (= 200 (:status (request "/check" app))))
    (is (= "OK" (:body (request "/check" app))))))

(deftest test-shortest-path-route-from-a-to-d
  (testing "If the shortest path returns the expected result"
    (is (= (write-str {:path ["a" "b" "d"] :cost 25}) (:body (request "/shortest-path/a/d" app))))))

(deftest test-shortest-cost-route-from-a-to-d
  (testing "If the shortest cost returns the expected result"
    (is (= (write-str 25) (:body (request "/shortest-path/a/d/cost" app))))))
