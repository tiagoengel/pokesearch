(ns pokesearch.core-test
  (:require [clojure.test :refer :all]
            [pokesearch.test-helper :refer :all]
            [clj-http.fake :refer :all]
            [clj-http.client :as c]
            [pokesearch.core :refer :all]))

(deftest get-specie-description-test
  (testing "get the first english description when available"
    (is (= (get-specie-description (load-edm-fixture "flavor_text_entries.edm"))
           "first desc")))

  (testing "get the first description when no english description is available"
    (is (= (get-specie-description (load-edm-fixture "flavor_text_entries_no_en_desc.edm"))
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

(deftest find-pokemon-test
  (with-fake-routes-in-isolation
    {"http://pokeapi.co/api/v2/pokemon/find-pokemon-test" (fixture-mock "pikachu.json")
     "http://pokeapi.co/api/v2/pokemon-species/find-pokemon-test" (fixture-mock "pikachu_specie.json")}

    (let [response (find-pokemon-by-name "find-pokemon-test")]
      (is (not (nil? response)))
      (is (= (-> response :name) "pikachu"))
      (is (= (-> response :image) "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png"))
      (is (= (-> response :attack) 55))
      (is (= (-> response :defense) 40))
      (is (= (-> response :description) "This Pokémon has electricity-storing pouches on its cheeks.\nThese appear to become electrically charged during the night\nwhile Pikachu sleeps. It occasionally discharges electricity\nwhen it is dozy after waking up.")))))
