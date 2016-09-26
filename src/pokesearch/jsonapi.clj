(ns pokesearch.jsonapi
  (:require [cheshire.core :as json]
            [ring.middleware.format-response :as response]))

(defprotocol JsonAPI
  (->jsonapi [x]))

(def encoder
  (response/make-encoder
    (fn [data] (json/generate-string (->jsonapi data)))
    "application/vnd.api+json"))
