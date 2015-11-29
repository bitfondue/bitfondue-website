(ns bitfondue.models.users
  (:require (bitfondue [config :as config]
                       [database :as database])))

(defn insert-user!
  "Insert a user into the database"
  [user]
  (database/insert-user<! config/database
                          (:username user)
                          (:email user)
                          (:password user)))

(defn insert-user!
  "Insert a user into the database"
  [user]
  (database/insert-user<! user))

(defn get-user
  "Get a user by its username"
  [username]
  (database/get-user {:username username}))
