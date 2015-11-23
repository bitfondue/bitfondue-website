(ns ^:figwheel-always bitfondue.core
    (:require [reagent.core :as reagent :refer [atom]]
              [ajax.core :refer [GET]]
              [secretary.core :as secretary :refer-macros [defroute]]
              [goog.events :as events]
              [goog.history.EventType :as EventType])
    (:import goog.History))

;; define your app data so that it doesn't get over-written on reload

(defonce chunks-state (atom '()))

;; ---
;; View Template
;; ---

(defn chunk-card
  [chunk]
  [:div.col-sm-4
   [:div.card.card-block
    [:h4.card-title (let [character-max-length 20]
                      (if (> character-max-length (count (:title chunk)))
                        (:title chunk)
                        (apply str (concat (take character-max-length (:title chunk))
                                           '("...")))))]]])

(defn chunks
  []
  [:div.chunks
   (for [chunk-row (->> @chunks-state
                        (sort-by :created_on)
                        (partition-all 3))]
     [:div.row
      (for [item chunk-row]
        [chunk-card item])])])

(defn header
  []
  [:nav.navbar.navbar-light.bg-faded
   [:div.container
    [:a.navbar-brand {:href "#"} "bitfondue"]
    [:ul.nav.navbar-nav
     [:li.nav-item
      [:a.nav-link {:href "#features"} "Features"]]
     [:li.nav-item
      [:a.nav-link {:href "#pricing"} "Pricing"]]
     [:li.nav-item
      [:a.nav-link {:href "#about"} "About"]]]
    [:ul.nav.navbar-nav.pull-right
     [:li.nav-item
      [:a.nav-link {:href "#login"} "Login"]]
     [:li.nav-item
      [:a.nav-link {:href "#register"} "Register"]]]]])

(defn layout
  [content]
  [:div
   [header]
   [:br]
   [:div.container content]])

(defn home-anonymous []
  [layout [:h1 "Home"]])

(defn home-authenticated []
  [layout [chunks]])

(defn features []
  [layout [:h1 "Features"]])

(defn pricing []
  [layout [:h1 "Pricing"]])

(defn about []
  [layout [:h1 "About"]])

(def login-form
  [:div.col-md-offset-3.col-md-6
   [:h1 "Login"]
   [:br]
   [:form
    [:fieldset.form-group
     [:label {:for "usernameInput"} "Username"]
     [:input.form-control {:type "email" :id "usernameInput" :placeholder "Your username please."}]]
    [:fieldset.form-group
     [:label {:for "passwordInput"} "Password"]
     [:input.form-control {:type "password" :id "passwordInput" :placeholder "Password"}]]
    [:button.btn.btn-primary {:type "submit"} "Login"]]])

(def register-form
  [:div.col-md-offset-3.col-md-6
   [:h1 "Register"]
   [:br]
   [:form
    [:fieldset.form-group
     [:label {:for "usernameInput"} "Username"]
     [:input.form-control {:type "email" :id "usernameInput" :placeholder "Your username please."}]]
    [:fieldset.form-group
     [:label {:for "passwordInput"} "Password"]
     [:input.form-control {:type "password" :id "passwordInput" :placeholder "Password"}]]
    [:fieldset.form-group
     [:label {:for "passwordInputRepeat"} "Password"]
     [:input.form-control {:type "password" :id "passwordInputRepeat" :placeholder "Password again"}]]
    [:button.btn.btn-primary {:type "submit"} "Login"]]])

(defn login []
  [layout [:div.row login-form]])

(defn register []
  [layout [:div.row register-form]])

(defn not-found []
  [layout [:h1 "404 - Page not found"]])

;; ---
;; Load data from the API
;; ---

(defn chunks-success-handler
  [response]
  (reset! chunks-state (:chunks response)))

(defn chunks-error-handler
  [{:keys [status status-text]}]
  nil)

(defn fetch-chunks
  []
  (GET "/chunks" {:response-format :json
                  :keywords? true
                  :handler chunks-success-handler
                  :error-handler chunks-error-handler}))

;; render the dom
(def application
  (js/document.getElementById "app"))

(defn page [page-component]
  (reagent/render-component
   [page-component]
   application))

;; set secretary to use # for the routing, eg. /#dashboard
(secretary/set-config! :prefix "#")

(defroute "/" [] (page home-anonymous))
(defroute "/dashboard" [] (page home-authenticated))
(defroute "/features" [] (page features))
(defroute "/pricing" [] (page pricing))
(defroute "/about" [] (page about))
(defroute "/login" [] (page login))
(defroute "/register" [] (page register))
(defroute "*" [] (page not-found))

;; Quick and dirty history configuration.
(let [h (History.)]
  (goog.events/listen h EventType/NAVIGATE #(secretary/dispatch! (.-token %)))
  (doto h (.setEnabled true)))

;; load the data from the API
(fetch-chunks)

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  (fetch-chunks)
)
