(ns pokesearch.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [pokesearch.handler :refer :all]))


(deftest test-app
  (testing "respond to /api/:name"
    (let [response (app (-> (request :get "/api/pikachu")))]
      (is (= 200 (:status response))))))
