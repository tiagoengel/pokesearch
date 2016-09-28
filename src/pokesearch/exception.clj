(ns pokesearch.exception
  (:require [compojure.api.exception :as ex]
            [ring.util.http-status :as status]
            [clojure.tools.logging :as log]
            [clojure.stacktrace :refer [root-cause]]
            [pokesearch.jsonapi :as jsonapi]))

(defrecord RequestError [status message]
  jsonapi/JsonAPI
  (->jsonapi [this]
    {:errors [{:status (str status) :title message}]}))

(defn pokesearch-default-handler
  "Maps excetions that have a `status` property to the correct http error.
  If the exception don't have a `status` property, returns 500"
  ([^Exception e data request]
   (pokesearch-default-handler e data request true))
  ([^Exception e data request log-ex]
   (let [status (or (get data :status) (get (ex-data (root-cause e)) :status) 500)]
     (if (and log-ex (= 500 status))
       (log/error e (.getMessage e)))
     {:status status :body (->RequestError status (status/get-name status))})))

(def handlers
  {::ex/default pokesearch-default-handler})
