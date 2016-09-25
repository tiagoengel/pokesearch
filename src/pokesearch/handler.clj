(ns pokesearch.handler
  (:require [compojure.api.sweet :refer :all]
            [pokesearch.exception :as ex]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [pokesearch.core :refer [find-pokemon-by-name]]))

(s/defschema Pokemon
  {:name s/Str
   :description s/Str
   :image s/Str
   :attack s/Int
   :defense s/Int})

(def app
  (api
    {:format {:formats [:json]}
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
