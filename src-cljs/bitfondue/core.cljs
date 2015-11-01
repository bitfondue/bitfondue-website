(ns ^:figwheel-always bitfondue.core
    (:require [reagent.core :as reagent :refer [atom]]
              [ajax.core :refer [GET]]
              [secretary.core :as secretary :refer-macros [defroute]]))

;; define your app data so that it doesn't get over-written on reload

(defonce chunks-state (atom '()))

;; ---
;; View Template
;; ---

(defn chunks
  []
  [:ul
   (for [item (->> @chunks-state
                   (sort-by :created_on))]
     [:li (:title item)])])

(defn header
  []
  [:nav.navbar.navbar-default
   [:div.container-fluid
    [:div.navbar-header
     [:a.navbar-brand "bitfondue"]]]])

(defn layout
  []
  [:div
   [header]
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
