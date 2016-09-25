(defproject pokesearch "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [metosin/compojure-api "1.1.8"]]
  :ring {:handler pokesearch.handler/app}
  :uberjar-name "server.jar"
  :profiles {:dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]
                                  [ring/ring-mock "0.3.0"]
                                  [clj-http-fake "1.0.2"]]
                   :plugins [[ikitommi/lein-ring "0.9.8-FIX"]
                             [com.jakemccrary/lein-test-refresh "0.17.0"]]}})
