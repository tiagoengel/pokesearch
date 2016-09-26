(ns pokesearch.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
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

    (context "/api" []
      :tags ["api"]

      (GET "/:name" [name]
        :return Pokemon
        :summary "find a pokemon by its name"
        (ok (find-pokemon-by-name name))))))
