(ns pokesearch.handler
  (:gen-class)
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [ring.util.response :refer [redirect]]
            [ring.adapter.jetty :as jetty]
            [environ.core :refer [env]]
            [clojure.tools.logging :as log]
            [pokesearch.exception :as ex]
            [pokesearch.jsonapi :as jsonapi]
            [pokesearch.core :refer :all])
  (:import  [pokesearch.core Pokemon]))

(def app
  (api
    {:format {:formats [:json jsonapi/encoder]}
     :exceptions {:handlers ex/handlers}
     :swagger {:ui "/docs"
               :spec "/swagger.json"
               :data {:info {:title "Pokesearch"
                             :description "Pokesearch API"}
                      :tags [{:name "api", :description ""}]}}}

    (GET "/" []
      :no-doc true
      (redirect "/docs"))

    (context "/api" []
      :tags ["api"]

      (GET "/:name" [name]
        :return Pokemon
        :summary "find a pokemon by its name"
        (ok (find-pokemon-by-name name))))))


(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 3000))]
    (log/info (str "Starting the server in " (or (and (env :production) "production") "development")))
    (jetty/run-jetty app {:port port :join? false})))
