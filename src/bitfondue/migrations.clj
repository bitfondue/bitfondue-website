(ns bitfondue.migrations
  (:require (ragtime [jdbc :as jdbc]
                     [repl :as repl])
            [bitfondue.config :as config]))

(defn load-config []
  {:datastore  (jdbc/sql-database config/database)
   :migrations (jdbc/load-resources "migrations")})

(defn migrate []
  (repl/migrate (load-config)))

(defn rollback []
  (repl/rollback (load-config)))

