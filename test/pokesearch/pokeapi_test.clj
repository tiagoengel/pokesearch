(ns pokesearch.pokeapi-test
  (:use pokesearch.test-helper)
  (:use clj-http.fake)
  (:require [clojure.test :refer :all]
            [clj-http.client :as c]
            [pokesearch.pokeapi :refer :all]))


(deftest url-test
  (is (= (url "pokemon" 1)) (str "http://pokeapi.co/api/v2/pokemon/" 1))
  (is (= (url "pokemon" "pikachu")) (str "http://pokeapi.co/api/v2/pokemon/pikachu"))
  (is (= (url "pokemon" 1 2 3 4)) (str "http://pokeapi.co/api/v2/pokemon/" 1 2 3 4)))

(deftest get-spicie-description-test
  (testing "get the first english description when available"
    (is (= (get-spicie-description (load-edm-fixture "flavor_text_entries.edm"))
           "first desc")))

  (testing "get the first description when no english description is available"
    (is (= (get-spicie-description (load-edm-fixture "flavor_text_entries_no_en_desc.edm"))
           "primeira desc"))))

(deftest get-pokemon-image-test
  (testing "get the `front_default` image when available"
    (is (= (get-pokemon-image (load-edm-fixture "pokemon_sprites.edm"))
           "front.jpg")))

  (testing "get the first image when the `front_default` image is not available"
    (is (= (get-pokemon-image (load-edm-fixture "pokemon_sprites_no_default.edm"))
           "first.jpg"))))

(deftest get-pokemon-stat-test
  (let [stats (load-edm-fixture "pokemon_stats.edm")]
    (is (= (get-pokemon-stat stats "attack") 50))
    (is (= (get-pokemon-stat stats "defense") 40))
    (is (= (get-pokemon-stat stats "speed") 90))
    (is (= (get-pokemon-stat stats "notfound") 0))))

(deftest pokeapi-test
  (testing "find pokemon by name"
    (with-fake-routes-in-isolation
      {"http://pokeapi.co/api/v2/pokemon/pikachu" (fixture-mock "pikachu.json")
       "http://pokeapi.co/api/v2/pokemon-species/pikachu" (fixture-mock "pikachu_specie.json")}

      (let [response (find-pokemon-by-name "pikachu")]
        (is (not (nil? response)))
        (is (= (-> response :name) "pikachu"))
        (is (= (-> response :image) "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png"))
        (is (= (-> response :atack) 55))
        (is (= (-> response :defense) 40))
        (is (= (-> response :description) "This Pokémon has electricity-storing pouches on its cheeks.\nThese appear to become electrically charged during the night\nwhile Pikachu sleeps. It occasionally discharges electricity\nwhen it is dozy after waking up."))))))
