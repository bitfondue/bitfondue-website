(ns bitfondue.handler
  (:require (compojure [core :refer :all]
                       [route :as route])))

(defroutes app-routes
  (GET "/" [] "Success!"))

(def app app-routes)
