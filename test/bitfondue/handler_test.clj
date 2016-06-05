(ns bitfondue.handler-test
  (:require [bitfondue.handler :refer :all]
            [ring.mock.request :as mock])
  (:use midje.sweet))

(fact "The root route / is returning a 200 status code"
  (->> (mock/header (mock/request :get "/") "X-Forwarded-Proto" "https")
       app
       :status) => 200)
