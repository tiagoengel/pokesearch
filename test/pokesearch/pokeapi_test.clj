(ns pokesearch.pokeapi-test
  (:require [clojure.test :refer :all]
            [pokesearch.test-helper :refer :all]
            [clj-http.fake :refer :all]
            [clj-http.client :as c]
            [pokesearch.pokeapi :refer :all]))

(deftest pokeapi-url-test
  (is (= (url "pokemon" 1)) (str "http://pokeapi.co/api/v2/pokemon/" 1))
  (is (= (url "pokemon" "pikachu")) (str "http://pokeapi.co/api/v2/pokemon/pikachu"))
  (is (= (url "pokemon" 1 2 3 4)) (str "http://pokeapi.co/api/v2/pokemon/" 1 2 3 4)))

(deftest pokeapi-test
  (testing "fetch pokemon by name"
    (with-fake-routes-in-isolation
      {"http://pokeapi.co/api/v2/pokemon/pikachu" (fixture-mock "pikachu.json")}

      (let [response (fetch-pokemon-by-name "pikachu")]
        (is (not (nil? response)))
        (is (= (-> response :name) "pikachu")))))

  (testing "fetch pokemon specie by name"
    (with-fake-routes-in-isolation
      {"http://pokeapi.co/api/v2/pokemon-species/pikachu" (fixture-mock "pikachu_specie.json")}

      (let [response (fetch-specie-by-name "pikachu")]
        (is (not (nil? response)))
        (is (= (-> response :name) "pikachu"))))))
