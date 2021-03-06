(ns bitfondue.handler
  (:require (compojure [core :refer :all]
                       [route :as route]
                       [response :refer [render]])
            [clojure.java.io :as io]
            [ring.util.response :refer [response redirect content-type status]]
            (ring.middleware [session :refer [wrap-session]]
                             [params :refer [wrap-params]]
                             [multipart-params :refer [wrap-multipart-params]]
                             [keyword-params :refer [wrap-keyword-params]]
                             [json :refer [wrap-json-response wrap-json-body wrap-json-params]]
                             [basic-authentication :refer [wrap-basic-authentication]]
                             [ssl :refer [wrap-ssl-redirect wrap-forwarded-scheme]])
            [hiccup.core :as h]
            [clj-time.core :as time]
            [buddy.sign.jwt :as jwt]
            [buddy.sign.jwe :as jwe]
            [buddy.core.nonce :as nonce]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [buddy.auth.backends.token :refer [jwe-backend]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            (bouncer [core :as b]
                     [validators :as v])
            [taoensso.timbre :as log]
            [bitfondue.config :as config]
            (bitfondue.models [users :as users]
                              [chunks :as chunks])
            [environ.core :refer [env]])
  (:use [ring.adapter.jetty :as ring]
        [clojure.pprint]
        [amazonica.aws.s3]
        [amazonica.aws.s3transfer :exclude [upload]])
  (:gen-class))

(def secret (nonce/random-bytes 32))

(defn ok [d] {:status 200 :body d})
(defn bad-request [d] {:status 400 :body d})

(defn chunks
  [request]
  (ok {:chunks (chunks/get-chunks)}))

(defn home
  [request]
  (if-not (authenticated? request)
    (throw-unauthorized)
    (ok {:status "Logged" :message (str "hello logged user "
                                        (:identity request))})))

(def authdata {:admin "secret"
               :test "secret"})

(defn login
  [request]
  (let [username (get (:params request) "username")
        password (get (:params request) "password")
        valid? (some-> authdata
                       (get (keyword username))
                       (= password))]
    (if valid?
      (let [claims {:user (keyword username)
                    :exp (time/plus (time/now) (time/seconds 3600))}
            token (jwt/encrypt claims secret {:alg :a256kw :enc :a128gcm})]
        (ok {:token token}))
      (bad-request {:message "wrong auth data"}))))

(defn upload
  [request]
  ;; make sure the body.img, body.html and body.tab_info attributes are present
  (log/debug "received a request")
  (spit "validated_request.edn" (b/validate (:params request)
                                            :username v/required
)
                                            :email    v/required
                                            :img      v/required
                                            :tab_info v/required
                                            :html     v/required))
(def serve-html5-app
  (slurp "resources/public/index.html"))

(defroutes app-routes
  (GET "/" [] serve-html5-app)
  (GET "/features" [] serve-html5-app)
  (GET "/pricing" [] serve-html5-app)
  (GET "/about" [] serve-html5-app)
  (GET "/login" [] serve-html5-app)
  (GET "/register" [] serve-html5-app)
  (GET "/dashboard" [] serve-html5-app)
  (GET "/chunks" [] chunks)
  (POST "/login" [] login)
  (POST "/upload" [] upload)
  (route/resources "/")
  (route/not-found "Not Found"))

(def auth-backend (jwe-backend {:secret secret
                                :options {:alg :a256kw :enc :a128gcm}}))

(defn basic-authenticated?
  "Basic authentication for the staging server"
  [username password]
  (and (= username (env :basic-authentication-username))
       (= password (env :basic-authentication-password))))

(defn wrap-if
  "Only add a certain middleware if conditions are met."
  [handler pred wrapper & args]
  (if pred
    (apply wrapper handler args)
    handler))

(def app (-> app-routes
             (wrap-authorization auth-backend)
             (wrap-authentication auth-backend)
             (wrap-json-response {:pretty false})
             (wrap-json-body {:keywords? true :bigdecimals? true})
             (wrap-multipart-params)
             (wrap-params)
             (wrap-keyword-params)
             (wrap-json-params)
             (wrap-if (not (or (nil? (env :basic-authentication-username))
                               (nil? (env :basic-authentication-password))))
                      wrap-basic-authentication basic-authenticated?)
             (wrap-ssl-redirect config/ssl)
             (wrap-forwarded-scheme)))

(defn -main [port]
  (run-jetty app {:port (read-string port) :join? false}))
