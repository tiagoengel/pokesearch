(ns pokesearch.test-helper
  (:require [clojure.java.io :as io]
            [pokesearch.pokeapi :as papi]))

(defn load-text-fixture
  "Loads the contents of a file from within resources/fixtures"
  [fixture]
  (->> (str "test/resources/fixtures/" fixture)
       slurp))

(defn load-edm-fixture
 "Loads the contents of a file from within resources/fixtures
 and transforms it to a clojure structure"
 [fixture]
 (read-string (load-text-fixture fixture)))

(defmacro fixture-mock
  "Generates mock definitions that responds with the content of a fixture file."
  [fixture]
  `(fn [request#] {:status 200
                   :headers {:content-type "application/json"}
                   :body (load-text-fixture ~fixture)}))
