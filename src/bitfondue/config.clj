(ns bitfondue.config
  (:require [environ.core :refer [env]]))

(def database
  (env :database-url))

;; Authentication & Authorization
(def token-secret
  (env :token-secret))

;; AWS credentials
(def aws-credentials
  {:access-key (env :aws-key)
   :secret-key (env :aws-secret)
   :bucket     (env :aws-bucket)})

(def email
  {:user (env :sendgrid-username)
   :pass (env :sendgrid-password)
   :host "smtp.sendgrid.net"
   :port 587})
