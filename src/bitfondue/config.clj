(ns bitfondue.config
  (:require [environ.core :as env]))

(def database
  (env :database-url))
