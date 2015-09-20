(ns bitfondue.migrations
  (:require (ragtime [jdbc :as jdbc]
                     [repl :as repl])
            [bitfondue.config :as config]
            [cheshire.core :refer :all]))

;; Database Migrations

(defn ^:private load-config
  "Create the configuration map for the migration/rollback functions"
  []
  {:datastore  (jdbc/sql-database config/database)
   :migrations (jdbc/load-resources "migrations")})

(defn migrate
  "Helper function to be called to run the database migrations against the current environment settings"
  []
  (repl/migrate (load-config)))

(defn rollback
  "Helper function to be called to run the database rollbacks against the current environment settings"
  []
  (repl/rollback (load-config)))


;; migrate the existing data into the new database
;; parse the json line entries into a collection of maps
(def ed (with-open [rdr (clojure.java.io/reader "existing_data.json")]
          (doall (->> rdr
                      line-seq
                      (map #(parse-string % true))))))
