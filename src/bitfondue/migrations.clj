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


;; Migrate the existing data into the new database
(defn ^:private parse-json-line-items
  "Parses the current .json line item file into a collection of maps"
  [file-name]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (doall (->> rdr
                line-seq
                (map #(parse-string % true))))))
