(ns pokesearch.handler-test
  (:require [clojure.test :refer :all]
            [pokesearch.test-helper :refer :all]
            [clj-http.fake :refer :all]
            [cheshire.core :as cheshire]
            [ring.mock.request :refer :all]
            [pokesearch.handler :refer :all]))

(defn parse-body
  [body]
  (cheshire/parse-string (slurp body) true))

(deftest api-test
  (testing "responds with 200 to a valid pokemon name"
    (with-fake-routes-in-isolation
      {"http://pokeapi.co/api/v2/pokemon/pikachu" (fixture-mock "pikachu.json")
       "http://pokeapi.co/api/v2/pokemon-species/pikachu" (fixture-mock "pikachu_specie.json")}

      (let [response (app (-> (request :get "/api/pikachu")))
            pokemon  (parse-body (:body response))]
        (is (= 200 (:status response)))
        (is (= (-> pokemon :name) "pikachu"))
        (is (= (-> pokemon :image) "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png"))
        (is (= (-> pokemon :attack) 55))
        (is (= (-> pokemon :defense) 40))
        (is (= (-> pokemon :description) "This Pokémon has electricity-storing pouches on its cheeks.\nThese appear to become electrically charged during the night\nwhile Pikachu sleeps. It occasionally discharges electricity\nwhen it is dozy after waking up.")))))


  (testing "responds with 404 to a invalid pokemon name"
    (with-fake-routes-in-isolation
      {"http://pokeapi.co/api/v2/pokemon/not-found" (fn [rewq] {:status 404 :body "not found"})}

      (let [response (app (-> (request :get "/api/not-found")))
            body     (parse-body (:body response))]
        (is (= 404 (:status response)))
        (is (= "Not Found" (:message body)))))))
