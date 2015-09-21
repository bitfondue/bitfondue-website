(ns bitfondue.database
  (:require [yesql.core :refer [defquery]]))

(defquery insert-user<! "bitfondue/sql/insert-user.sql")
(defquery get-user "bitfondue/sql/get-user.sql")

