(ns bitfondue.models.chunks
  (:require (bitfondue [config :as config]
                       [database :as database])
            [cheshire.core :as cheshire]))

(defn insert-chunk!
  "Insert a chunk into the database"
  [chunk]
  (database/insert-chunk<! (assoc chunk
                                  :data (cheshire/generate-string (:data chunk)))))

(defn get-chunks
  "Retrieve chunks"
  []
  (database/get-chunks))
