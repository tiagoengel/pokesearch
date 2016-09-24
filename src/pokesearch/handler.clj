(ns pokesearch.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]))

(def app
  (api
    {:format {:formats [:json]}
     :swagger {:ui "/docs"
               :spec "/swagger.json"
               :data {:info {:title "Pokesearch"
                             :description "Pokesearch API"}
                      :tags [{:name "api", :description ""}]}}}

    (context "/api" []
      :tags ["api"]

      (GET "/:name" [name]
        :return {:result s/Str}
        :summary "find a pokemon by its name"
        (ok {:result name})))))
