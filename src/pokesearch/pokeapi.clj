(ns pokesearch.pokeapi
  (:require [clj-http.client :as client]))

(def base-url "http://pokeapi.co/api/v2")

(defn url
  "Builds a url for querying the pokeapi"
  [& colls]
  (->> (cons base-url colls)
       (interpose "/")
       (apply str)))

(defn fetch-specie-by-name
  "Fetchs a pokemon specie from the pokeapi"
  [name]
  (-> (client/get (url "pokemon-species" name) {:as :json})
      :body))

(defn fetch-pokemon-by-name
  "Fetchs a pokemon from the pokeapi"
  [name]
  (-> (client/get (url "pokemon" name) {:as :json})
      :body))
