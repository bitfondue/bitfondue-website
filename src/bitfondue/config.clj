(ns bitfondue.config
  (:require [environ.core :refer [env]]))

(def database
  (env :database-url))

;; Authentication & Authorization
(def token-secret
  (env :token-secret))
