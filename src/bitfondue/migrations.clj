(ns bitfondue.migrations
  (:require (ragtime [jdbc :as jdbc]
                     [repl :as repl])
            [bitfondue.config :as config]
            [cheshire.core :refer :all]))

(defn load-config []
  {:datastore  (jdbc/sql-database config/database)
   :migrations (jdbc/load-resources "migrations")})

(defn migrate []
  (repl/migrate (load-config)))

(defn rollback []
  (repl/rollback (load-config)))


;; migrate the existing data into the new database
;; parse the json line entries into a collection of maps
(def ed (with-open [rdr (clojure.java.io/reader "existing_data.json")]
          (doall (->> rdr
                      line-seq
                      (map #(parse-string % true))))))
