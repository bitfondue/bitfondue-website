(ns bitfondue.handler-test
  (:require [bitfondue.handler :refer :all]
            [ring.mock.request :as mock])
  (:use midje.sweet))

(fact "The root route / is returning a 200 status code"
  (->> (mock/request :get "/")
       app
       :status) => 200)
