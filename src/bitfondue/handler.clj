(ns bitfondue.handler
  (:require (compojure [core :refer :all]
                       [route :as route]))
  (:use [ring.adapter.jetty :as ring]))

(defroutes app-routes
  (GET "/" [] "Success!"))

(def app app-routes)

(defn -main [port]
  (run-jetty app {:port (read-string port) :join? false}))
