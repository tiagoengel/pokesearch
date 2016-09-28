(defproject pokesearch "0.1.0-SNAPSHOT"
  :description "A REST api for finding pokémons"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [metosin/compojure-api "1.1.8"]
                 [slingshot "0.12.2"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.clojure/core.cache "0.6.4"]
                 [clj-http "2.3.0"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [environ "1.0.0"]]

  :min-lein-version "2.0.0"

  :ring {:handler pokesearch.handler/app}
  :main pokesearch.handler

  :plugins [[environ/environ.lein "0.3.1"]]
  :hooks [environ.leiningen.hooks]
  :uberjar-name "server.jar"
  :profiles {:uberjar {:aot :all
                       :env {:production true}}
             :dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]
                                  [ring/ring-mock "0.3.0"]
                                  [clj-http-fake "1.0.2"]]
                   :plugins [[ikitommi/lein-ring "0.9.8-FIX"]
                             [com.jakemccrary/lein-test-refresh "0.17.0"]]}})
