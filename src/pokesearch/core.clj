(ns pokesearch.core
  (:require [schema.core :as s]
            [clojure.string :refer [lower-case]]
            [pokesearch.pokeapi :as api]
            [pokesearch.jsonapi :as jsonapi]))

(s/defrecord Pokemon [name        :- s/Str,
                      description :- s/Str,
                      image       :- s/Str,
                      attack      :- s/Int,
                      defense     :- s/Int]
  jsonapi/JsonAPI
  (->jsonapi [this]
    {:data {:type "pokemon" :id (str name) :attributes this}}))

(defn- eq-in
  [data path val]
  (= (get-in data path) val))

(defn get-specie-description
  "Gets the first english description for the species or the first description
  if no english description is found"
  [specie]
  (let [text-entries (:flavor_text_entries specie)
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

(defn find-pokemon-by-name
  "Finds a pokemon by its name"
  [name]
  (when-let [normalized-name (some-> name lower-case)]
    (let [requests [(future (api/fetch-pokemon-by-name normalized-name))
                    (future (api/fetch-specie-by-name normalized-name))]
          [pokemon specie] (map deref requests)]
      (map->Pokemon {:name (:name pokemon)
                     :image (get-pokemon-image pokemon)
                     :attack (get-pokemon-stat pokemon "attack")
                     :defense (get-pokemon-stat pokemon "defense")
                     :description (get-specie-description specie)}))))
