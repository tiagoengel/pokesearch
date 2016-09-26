(ns pokesearch.jsonapi-test
  (:require [clojure.test :refer :all]
            [cheshire.core :as json]
            [pokesearch.jsonapi :refer :all]))

(defrecord TestResource [id name]
  JsonAPI
  (->jsonapi [this]
    {:type "test" :id id :name name}))

(deftest jsonapi-encoder-test
  (is (= (:content-type encoder) "application/vnd.api+json"))
  (is (= ((:encoder encoder) (->TestResource 1 "teste"))
         (json/generate-string {:type "test" :id 1 :name "teste"}))))
