(ns ^:figwheel-always bitfondue.core

;; define your app data so that it doesn't get over-written on reload

  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET]]
            [secretary.core :as secretary :refer-macros [defroute]]
            [goog.events :as events])
  (:import [goog.history Html5History EventType]))

;; Make the Html 5 History API work
(defn get-token []
  (str js/window.location.pathname js/window.location.search))

(defn make-history []
  (doto (Html5History.)
    (.setPathPrefix (str js/window.location.protocol
                         "//"
                         js/window.location.host))
    (.setUseFragment false)))

(defn handle-url-change [e]
  ;; log the event object to console for inspection
  (js/console.log e)
  ;; and let's see the token
  (js/console.log (str "Navigating: " (get-token)))
  ;; we are checking if this event is due to user action,
  ;; such as click a link, a back button, etc.
  ;; as opposed to programmatically setting the URL with the API
  (when-not (.-isNavigation e)
    ;; in this case, we're setting it
    (js/console.log "Token set programmatically")
    ;; let's scroll to the top to simulate a navigation
    (js/window.scrollTo 0 0))
  ;; dispatch on the token
  (secretary/dispatch! (get-token)))

(defonce history (make-history))
(defonce chunks-state (atom '()))

;; ---
;; View Template
;; ---

(defn chunk-card
  [chunk]
  [:div.col-sm-4
   [:img.card-img-top {:src (str "/images/"
                                 (:uid chunk)
                                 ".png")}]
   [:div.card.card-block.chunk-content
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

(defn nav-helper
  "Creating the map so that links will be routed without server-refresh"
  [token]
  {:href token
   :on-click #(do
                (.preventDefault %)
                (.setToken history token))})

(defn header
  []
  [:nav.navbar.navbar-light.bg-faded
   [:div.container
    [:a.navbar-brand (nav-helper "/") "bitfondue"]
    [:ul.nav.navbar-nav
     [:li.nav-item
      [:a.nav-link (nav-helper "/features") "Features"]]
     [:li.nav-item
      [:a.nav-link (nav-helper "/pricing") "Pricing"]]
     [:li.nav-item
      [:a.nav-link (nav-helper "/about") "About"]]]
    [:ul.nav.navbar-nav.pull-right
     [:li.nav-item
      [:a.nav-link (nav-helper "/login") "Login"]]
     [:li.nav-item
      [:a.nav-link (nav-helper "/register") "Register"]]]]])

(defn footer
  []
  [:div.container
   [:div.row
    [:div.col-md-4
     [:ul
      [:li [:a {:href "http://blog.bitfondue.com"} "Blog"]]
      [:li [:a {:href "https://github.com/bitfondue"} "GitHub"]]]]
    [:div.col-md-4]
    [:div.col-md-4
     [:ul
      [:li [:a {:href "https://twitter.com/bitfondue"} "Twitter"]]
      [:li [:a {:href "https://twitter.com/bitfondue"} "Facebook"]]]]]])


(defn layout
  [content]
  [:div
   [header]
   [:br]
   [:div.container content]
   [:br]
   [footer]])

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

(defroute "/" [] (page home-anonymous))
(defroute "/dashboard" [] (page home-authenticated))
(defroute "/features" [] (page features))
(defroute "/pricing" [] (page pricing))
(defroute "/about" [] (page about))
(defroute "/login" [] (page login))
(defroute "/register" [] (page register))
(defroute "*" [] (page not-found))

;; init the html5-history support
(goog.events/listen history
                    EventType.NAVIGATE
                    #(handle-url-change %))
(.setEnabled history true)

;; load the data from the API
(fetch-chunks)

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  (fetch-chunks)
)
