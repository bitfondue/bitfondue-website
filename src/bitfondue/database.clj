(ns bitfondue.database
  (:require [yesql.core :refer [defquery]]))

(defquery insert-user<! "bitfondue/sql/insert-user.sql")

