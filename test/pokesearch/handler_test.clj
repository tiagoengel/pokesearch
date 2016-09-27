(ns pokesearch.handler-test
  (:require [clojure.test :refer :all]
            [pokesearch.test-helper :refer :all]
            [clj-http.fake :refer :all]
            [cheshire.core :as cheshire]
            [ring.mock.request :refer :all]
            [pokesearch.handler :refer :all]))

(def pikachu {:name "pikachu"
              :image "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png"
              :attack 55
              :defense 40
              :description "This Pokémon has electricity-storing pouches on its cheeks.\nThese appear to become electrically charged during the night\nwhile Pikachu sleeps. It occasionally discharges electricity\nwhen it is dozy after waking up."})

(defn parse-body
  [body]
  (cheshire/parse-string (slurp body) true))

(deftest api-test
  (testing "responds with 200 to a valid pokemon name"
    (with-fake-routes-in-isolation
      {"http://pokeapi.co/api/v2/pokemon/api-test" (fixture-mock "pikachu.json")
       "http://pokeapi.co/api/v2/pokemon-species/api-test" (fixture-mock "pikachu_specie.json")}

      (let [response (app (request :get "/api/api-test"))
            pokemon  (parse-body (:body response))]
        (is (= 200 (:status response)))
        (is (= pokemon pikachu)))

      (let [response (app (-> (request :get "/api/api-test") (content-type "application/vnd.api+json")))
            pokemon  (parse-body (:body response))]
        (is (= 200 (:status response)))
        (is (= "application/vnd.api+json; charset=utf-8" (get-in response [:headers "Content-Type"])))
        (is (= pokemon
               {:data {:type "pokemon"
                       :id "pikachu"
                       :attributes pikachu}})))))


  (testing "responds with 404 to a invalid pokemon name"
    (with-fake-routes-in-isolation
      {"http://pokeapi.co/api/v2/pokemon/not-found" (fn [rewq] {:status 404 :body "not found"})}

      (let [response (app (request :get "/api/not-found"))
            body     (parse-body (:body response))]
        (is (= 404 (:status response)))
        (is (= "Not Found" (:message body))))

      (let [response (app (-> (request :get "/api/not-found") (content-type "application/vnd.api+json")))
            body     (parse-body (:body response))]
        (is (= 404 (:status response)))
        (is (= "application/vnd.api+json; charset=utf-8" (get-in response [:headers "Content-Type"])))
        (is (= body
               {:errors [{:status "404"
                          :title "Not Found"}]}))))))
