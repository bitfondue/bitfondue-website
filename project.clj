(defproject bitfondue "0.1.0-SNAPSHOT"
  :description "The bitfondue website repository"
  :url "https://bitfondue.com"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [environ "1.0.0"]]
  :plugins [[lein-environ "1.0.0"]
            [codox "0.8.13"]]
  :codox {:src-dir-uri "https://github.com/bitfondue/bitfondue-website/blob/master/"
          :src-linenum-anchor-prefix "L"})
