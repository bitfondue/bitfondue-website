(ns bitfondue.handler
  (:require (compojure [core :refer :all]
                       [route :as route])))

(defroutes app-routes
  (GET "/" [] "Success!"))

(def app app-routes)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
