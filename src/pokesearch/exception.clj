(ns pokesearch.exception
  (:require [compojure.api.exception :as ex]
            [ring.util.http-status :as status]))

(defn pokesearch-default-handler
  "Maps excetions that have a `status` property to the correct http error.
  If the excetion don't have a `status` property, returns 500"
  [^Exception e data request]
  (let [status (get data :status 500)]
    {:status status :body {:message (status/get-name status)}}))

(def handlers
  {::ex/request-validation ex/request-validation-handler
   ::ex/request-parsing ex/request-parsing-handler
   ::ex/response-validation ex/response-validation-handler
   ::ex/default pokesearch-default-handler})
