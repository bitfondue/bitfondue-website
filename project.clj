(defproject bitfondue "0.1.0-SNAPSHOT"
  :description "The bitfondue website repository"
  :url "https://bitfondue.com"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [environ "1.0.0"]
                 [compojure "1.1.8"]
                 [ring/ring-jetty-adapter "1.2.2"]
                 [ring/ring-core "1.3.1"]]
  :plugins [[lein-environ "1.0.0"]
            [lein-ring "0.8.11"]
            [codox "0.8.13"]]
  :profiles {:dev {:dependencies [[clj-http-fake "1.0.1"]
                                  [midje "1.7.0" :exclusions [org.clojure/clojure]]
                                  [ring-mock "0.1.5"]]
                   :plugins [[lein-midje "3.1.3"]]}}
  :codox {:src-dir-uri "https://github.com/bitfondue/bitfondue-website/blob/master/"
          :src-linenum-anchor-prefix "L"})
