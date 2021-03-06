(ns bitfondue.migrations
  (:require (ragtime [jdbc :as jdbc]
                     [repl :as repl])
            [bitfondue.config :as config]
            (bitfondue.models [chunks :as chunks]
                              [users :as users])
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

(defn import-old-data
  "Parses the old database dump and imports it"
  []
  (doseq [item (parse-json-line-items "existing_data.json")]
    (let [c {:title (:title (:tab_info item))
             :url   (:url (:tab_info item))
             :description_full (:text (:diffbot item))
             :uid   (:uid item)}]
      (chunks/insert-chunk! c))))


