(ns ^:figwheel-always bitfondue.core
    (:require [reagent.core :as reagent :refer [atom]]
              [ajax.core :refer [GET]]
              [secretary.core :as secretary :refer-macros [defroute]]))

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
                                           '("...")))))]
    [:p.card-text "An excerpt of the content"]]])

(defn chunks
  []
  [:div.container
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
    [:a.navbar-brand {:href "/"} "bitfondue"]
    [:ul.nav.navbar-nav
     [:li.nav-item
      [:a.nav-link {:href "#"} "Features"]]
     [:li.nav-item
      [:a.nav-link {:href "#"} "Pricing"]]
     [:li.nav-item
      [:a.nav-link {:href "#"} "About"]]]
    [:ul.nav.navbar-nav.pull-right
     [:li.nav-item
      [:a.nav-link {:href "#"} "Login"]]
     [:li.nav-item
      [:a.nav-link {:href "#"} "Register"]]]]])

(defn layout
  []
  [:div
   [header]
   [:br]
   [chunks]])

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
(reagent/render-component [layout]
                          (. js/document (getElementById "app")))
  
;; load the data from the API
(fetch-chunks)

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  (fetch-chunks)
)
