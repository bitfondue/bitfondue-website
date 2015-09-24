(ns bitfondue.handler
  (:require (compojure [core :refer :all]
                       [route :as route]
                       [response :refer [render]])
            [clojure.java.io :as io]
            [ring.util.response :refer [response redirect content-type status]]
            (ring.middleware [session :refer [wrap-session]]
                             [params :refer [wrap-params]]
                             [json :refer [wrap-json-response wrap-json-body]])
            [hiccup.core :as h]
            [clj-time.core :as time]
            [buddy.sign.jwe :as jwe]
            [buddy.core.nonce :as nonce]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [buddy.auth.backends.token :refer [jwe-backend]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            (bitfondue.models [users :as users]))
  (:use [ring.adapter.jetty :as ring])
  (:gen-class))

(defroutes app-routes
  (GET "/" [] home)
  (POST "/login" [] login)
  (route/resources "/"))


(defn -main [port]
  (run-jetty app {:port (read-string port) :join? false}))
