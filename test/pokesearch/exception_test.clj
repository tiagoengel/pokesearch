(ns pokesearch.exception-test
  (:require [pokesearch.exception :refer :all]
            [ring.util.http-status :as status]
            [clojure.test :refer :all]))

(defn build-response
  [status]
  {:status status :body {:message (status/get-name status)}})

(deftest pokesearch-default-handler-test
  (testing "maps exceptions to the correct http status"
    (is (= (pokesearch-default-handler (Exception.) {:status 404} nil)
           (build-response 404)))
    (is (= (pokesearch-default-handler (Exception.) {:status 422} nil)
           (build-response 422)))
    (is (= (pokesearch-default-handler (Exception.) {:status 500} nil false)
           (build-response 500))))

  (testing "returns 500 when no status information is present"
    (is (= (pokesearch-default-handler (Exception.) {} nil false)
           (build-response 500)))))
