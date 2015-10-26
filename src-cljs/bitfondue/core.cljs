(ns ^:figwheel-always bitfondue.core
    (:require
     [reagent.core :as reagent :refer [atom]]
     [ajax.core :refer [GET]]))

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}))
(defonce chunks-state (atom '()))

(defn hello-world []
  [:h1 (:text @app-state)])

(defn chunks
  []
  [:ul
   (for [item (->> @chunks-state
                   (sort-by :created_on))]
     [:li (:title item)])])

(defn layout
  []
  [:div
   [hello-world]
   [chunks]])

(reagent/render-component [layout]
                          (. js/document (getElementById "app")))

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

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  (fetch-chunks)
)
