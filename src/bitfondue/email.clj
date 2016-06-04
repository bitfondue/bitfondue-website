(ns bitfondue.email
  (:require [postal.core :refer [send-message]]
            [bitfondue.config :as config]))

(defn say-hi
  []
  (send-message config/email
                {:from "from@bitfondue.com"
                 :to   "to@bitfondue.com"
                 :subject "Hello World"
                 :body "This is a test message"}))