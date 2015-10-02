(ns bitfondue.database
  (:require [cheshire.core :as cheshire]
            [yesql.core :refer [defqueries]]
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

(defqueries "queries.sql")

