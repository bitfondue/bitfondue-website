(ns bitfondue.config
  (:require [environ.core :refer [env]]))

(defn ^:private env-default
  "Read environment variable and return default if not provided."
  [env-key env-default-val]
  (let [env-val (env env-key)]
    (if (nil? env-val)
      env-default-val
      (if (number? env-default-val)
        (read-string env-val)
        env-val))))

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

(def ssl
  {:ssl-port (env-default :ssl-port 443)})
