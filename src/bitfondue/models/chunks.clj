(ns bitfondue.models.chunks
  (:require (bitfondue [config :as config]
                       [database :as database])))

(defn insert-chunk!
  "Insert a chunk into the database"
  [chunk]
  (database/insert-chunk<! chunk))

(defn get-chunks
  "Retrieve chunks"
  []
  (database/get-chunks))
