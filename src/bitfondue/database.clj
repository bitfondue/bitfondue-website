(ns bitfondue.database
  (:require [cheshire.core :as cheshire]
            [yesql.core :refer [defquery]]
            [clojure.java.jdbc :as jdbc])
  (:import [org.postgresql.util PGobject]))

(extend-type org.postgresql.util.PGobject
  jdbc/IResultSetReadColumn
  (result-set-read-column [val rsmeta idx]
    (let [colType (.getColumnTypeName rsmeta idx)]
      (if (or (= colType "json")
              (= colType "jsonb"))
        (cheshire/parse-string (.getValue val) true)
        val))))

(defquery insert-user<! "bitfondue/sql/insert-user.sql")
(defquery get-user "bitfondue/sql/get-user.sql")

