(defproject bitfondue "0.1.0-SNAPSHOT"
  :description "The bitfondue website repository"
  :url "https://bitfondue.com"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.122"]
                 [environ "1.0.0"]
                 [compojure "1.4.0"]
                 [ring/ring-core "1.3.2"]
                 [ring/ring-jetty-adapter "1.3.2"]
                 [ring/ring-json "0.3.1" :exclusions [ring/ring-core]]
                 [ragtime "0.5.2"]
                 [yesql "0.4.2"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [cheshire "5.5.0"]
                 [buddy/buddy-core "0.7.0"]
                 [buddy/buddy-auth "0.7.0"]
                 [hiccup "1.0.5"]]
                 [reagent "0.5.0"]]
  :plugins [[lein-environ "1.0.0"]
            [lein-ring "0.8.11"]
            [codox "0.8.13"]]
  :profiles {:dev {:dependencies [[clj-http-fake "1.0.1"]
                                  [midje "1.7.0" :exclusions [org.clojure/clojure]]
                                  [ring-mock "0.1.5"]]
                   :plugins [[lein-midje "3.1.3"]]}
             :uberjar {:plugins [[com.carouselapps/jar-copier "0.2.0"]]
                       :java-agents [[com.newrelic.agent.java/newrelic-agent "3.20.0"]]
                       :prep-tasks ["javac" "compile" "jar-copier"]
                       :jar-copier {:java-agents true
                                    :destination "resources/jars"}
                       :main bitfondue.handler, :aot :all}}
  :ring {:handler bitfondue.handler/app}
  :uberjar-name "bitfondue-standalone.jar"
  :main bitfondue.handler
  :min-lein-version "2.5.0"
  :codox {:src-dir-uri "https://github.com/bitfondue/bitfondue-website/blob/master/"
          :src-linenum-anchor-prefix "L"}
  :aliases {"migrate"  ["run" "-m" "bitfondue.migrations/migrate"]
            "rollback" ["run" "-m" "bitfondue.migrations/rollback"]})
