(ns pokesearch.pokeapi
  (:require [clj-http.client :as client]
            [clojure.core.cache :as cache]))

(def ^{:private true
       :doc "A time to leave cache configured to ensure we are following
       the pokeapi fair use policy and not doing more that 300 requests a day
       for each resource"}
  Cache (atom (cache/ttl-cache-factory {} :ttl (/ 86400000 250))))

(defn- get-or-update!
  "Gets or update a item from the cache.
  wraps the recommended has-hit-get pattern
  https://github.com/clojure/core.cache/wiki/Using"
  [key func]
  (if (cache/has? @Cache key)
    (get (swap! Cache #(cache/hit % key)) key)
    (get (swap! Cache #(cache/miss % key (func))) key)))

(defn url
  "Builds a url for querying the pokeapi"
  [& colls]
  (->> (cons "http://pokeapi.co/api/v2" colls)
       (interpose "/")
       (apply str)))

(defn execute-request
  "Issues a request to the pokeapi and caches the result.
  This function also ensures that multiple threads will not
  execute more than one request at the same time for the same url,
  thus making the cache consistent over multiple threads."
  [url]
  (force (get-or-update! (keyword url) #(delay (client/get url {:as :json})))))

(defn fetch-specie-by-name
  "Fetchs a pokemon specie from the pokeapi"
  [name]
  (-> (execute-request (url "pokemon-species" name)) :body))

(defn fetch-pokemon-by-name
  "Fetchs a pokemon from the pokeapi"
  [name]
  (-> (execute-request (url "pokemon" name)) :body))
