(defproject bitfondue "0.1.0-SNAPSHOT"
  :description "The bitfondue website repository"
  :url "https://bitfondue.com"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.7.122"]

                 ;; backend dependencies
                 [environ "1.0.3"]
                 [compojure "1.4.0"]
                 [ring/ring-core "1.3.2"]
                 [ring/ring-jetty-adapter "1.3.2"]
                 [ring/ring-json "0.3.1" :exclusions [ring/ring-core]]
                 [ragtime "0.5.2"]
                 [yesql "0.5.1"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [cheshire "5.5.0"]
                 [buddy/buddy-core "0.13.0"]
                 [buddy/buddy-auth "1.1.0"]
                 [hiccup "1.0.5"]
                 [bouncer "0.3.3"]
                 [com.taoensso/timbre "4.1.0"]
                 [amazonica "0.3.39"]
                 [ring-basic-authentication "1.0.5"]
                 [ring/ring-ssl "0.2.1"]
                 [com.draines/postal "2.0.0"]

                 ;; front-end dependencies
                 [reagent "0.5.0"]
                 [cljs-ajax "0.5.0"]
                 [secretary "1.2.3"]]

  :plugins [[lein-environ "1.0.0"]
            [lein-ring "0.8.11"]
            [codox "0.8.13"]
            [lein-cljsbuild "1.1.0"]
            [lein-figwheel "0.4.0"]]
  :clean-targets ^{:protect false} ["resources/public/js" "target"]
  :figwheel {
             ;; :http-server-root "public" ;; default and assumes "resources"
             ;; :server-port 3449 ;; default
             ;; :server-ip "127.0.0.1"

             :css-dirs ["resources/public/css"] ;; watch and update CSS

             :ring-handler bitfondue.handler/app}
  :cljsbuild {:builds {:app {:source-paths ["src-cljs"]
                              :figwheel {:on-jsload "bitfondue.core/on-js-reload"}
                              :compiler {:output-to     "resources/public/js/app.js"
                                         :output-dir    "resources/public/js/out"
                                         :source-map    "resources/public/js/out.js.map"
                                         :externs       ["react/externs/react.js"]
                                         :main          "bitfondue.core"
                                         :asset-path    "js/out"
                                         :optimizations :none
                                         :pretty-print  true}}}}
  :profiles {:dev {:dependencies [[clj-http-fake "1.0.1"]
                                  [midje "1.7.0" :exclusions [org.clojure/clojure]]
                                  [ring-mock "0.1.5"]]
                   :plugins [[lein-midje "3.1.3"]]}
             :uberjar {:plugins [[com.carouselapps/jar-copier "0.2.0"]]
                       :java-agents [[com.newrelic.agent.java/newrelic-agent "3.20.0"]]
                       :prep-tasks ["javac" "compile" "jar-copier"]
                       :jar-copier {:java-agents true
                                    :destination "resources/jars"}
                       :main bitfondue.handler, :aot :all

                       ;; compile the front-end
                       :hooks [leiningen.cljsbuild]
                       :cljsbuild {:jar true
                                   :builds {:app
                                            {:figwheel nil
                                             :compiler
                                             {:optimizations :advanced
                                              :pretty-print false}}}}}}

  :ring {:handler bitfondue.handler/app
         :nrepl {:start? true}}
  :uberjar-name "bitfondue-standalone.jar"
  :main bitfondue.handler
  :min-lein-version "2.5.0"
  :codox {:src-dir-uri "https://github.com/bitfondue/bitfondue-website/blob/master/"
          :src-linenum-anchor-prefix "L"}
  :aliases {"migrate"  ["run" "-m" "bitfondue.migrations/migrate"]
            "rollback" ["run" "-m" "bitfondue.migrations/rollback"]})
