(ns bitfondue.handler
  (:require (compojure [core :refer :all]
                       [route :as route])
            [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds])
            [ring.util.response :as resp]
            (bitfondue.models [users :as users]))
  (:use [ring.adapter.jetty :as ring]))

(defroutes app-routes
  (GET "/" [] "Success!")
  (route/resources "/"))

(def app (friend/authenticate
          app-routes
          {:allow-anon? true
           :login-uri "/login"
           :default-landing-uri "/"
           :unauthorized-handler #(-> "Not Authorized"
                                      resp/response
                                      (resp/status 401))
           :credential-fn #(creds/bcrypt-credential-fn users/get-user %)
           :workflows [(workflows/interactive-form)]}))

(defn -main [port]
  (run-jetty app {:port (read-string port) :join? false}))
