(ns pokesearch.pokeapi-test
  (:require [clojure.test :refer :all]
            [pokesearch.test-helper :refer :all]
            [clj-http.fake :refer :all]
            [clj-http.client :as c]
            [pokesearch.pokeapi :refer :all]))

(defn uuid [] (str (java.util.UUID/randomUUID)))

(deftest pokeapi-url-test
  (is (= (url "pokemon" 1) "http://pokeapi.co/api/v2/pokemon/1"))
  (is (= (url "pokemon" "pikachu") "http://pokeapi.co/api/v2/pokemon/pikachu"))
  (is (= (url "pokemon" 1 2 3 4) "http://pokeapi.co/api/v2/pokemon/1/2/3/4")))

(deftest pokeapi-execute-request-test
  (testing "consistent cache over multiple threads"
    (let [req-count (atom 0)
          url (str (java.util.UUID/randomUUID))]
      (with-redefs [c/get (fn [url opts] {:body {:count (swap! req-count inc)}})]
        (let [requests (take 5 (repeatedly #(future (execute-request url))))]
          (doall (map deref requests))
          (is (= @req-count 1))))))

 (testing "caches requests based on the url"
   (let [req-count (atom 0)
         url  (str (java.util.UUID/randomUUID))
         url2 (str (java.util.UUID/randomUUID))]
     (with-redefs [c/get (fn [url opts] {:body {:count (swap! req-count inc)}})]
       (let [create-reqs (fn [url] (take 5 (repeatedly #(future (execute-request url)))))
             requests (concat (create-reqs url) (create-reqs url2))]
        (doall (map deref requests))
        (is (= @req-count 2)))))))

(deftest pokeapi-test
  (testing "fetch pokemon by name"
    (with-fake-routes-in-isolation
      {"http://pokeapi.co/api/v2/pokemon/pokeapi-test" (fixture-mock "pikachu.json")}

      (let [response (fetch-pokemon-by-name "pokeapi-test")]
        (is (not (nil? response)))
        (is (= (-> response :name) "pikachu")))))

  (testing "fetch pokemon specie by name"
    (with-fake-routes-in-isolation
      {"http://pokeapi.co/api/v2/pokemon-species/pokeapi-test" (fixture-mock "pikachu_specie.json")}

      (let [response (fetch-specie-by-name "pokeapi-test")]
        (is (not (nil? response)))
        (is (= (-> response :name) "pikachu"))))))
