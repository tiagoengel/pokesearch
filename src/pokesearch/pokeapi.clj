(ns pokesearch.pokeapi
  (:import java.net.URLEncoder)
  (:require [clj-http.client :as client]
            [clojure.string :as str]))

(def base-url "http://pokeapi.co/api/v2")

(defn url
  "Builds a url for querying the pokeapi"
  [& colls]
  (->> (cons base-url colls)
       (interpose "/")
       (apply str)))

(defn- eq-in
  [data path val]
  (= (get-in data path) val))

(defn get-spicie-description
  "Gets the first english description for the spicies or the first description
  if no english description is found"
  [spicie]
  (let [text-entries (:flavor_text_entries spicie)
        en-desc (filter #(eq-in % [:language :name] "en") text-entries)]
    (-> (or (not-empty en-desc) text-entries) first :flavor_text)))

(defn get-pokemon-image
  "Gets the `front_default` image of this pokemon or the first found if
  no `front_default` image is available"
  [pokemon]
  (let [sprites (:sprites pokemon)]
    (or (:front_default sprites) (first (vals sprites)))))

(defn get-pokemon-stat
  "Gets the value of a pokemon stat"
  [pokemon stat-name]
  (let [stats (:stats pokemon)
        stat (first (filter #(eq-in % [:stat :name] stat-name) stats))]
    (or (some-> stat :base_stat) 0)))

(defn- fetch-spicie-by-name
  [name]
  (-> (client/get (url "pokemon-species" name) {:as :json})
      :body))

(defn- fetch-pokemon-by-name
  [name]
  (-> (client/get (url "pokemon" name) {:as :json})
      :body))

(defn find-pokemon-by-name
  "Finds a pokemon by its name"
  [name]
  (let [pokemon (fetch-pokemon-by-name name)
        spicie (fetch-spicie-by-name name)]
    {:name name
     :image (get-pokemon-image pokemon)
     :atack (get-pokemon-stat pokemon "attack")
     :defense (get-pokemon-stat pokemon "defense")
     :description (get-spicie-description spicie)}))
