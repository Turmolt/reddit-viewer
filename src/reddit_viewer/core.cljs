(ns reddit-viewer.core
    (:require
     [reagent.core :as r]
     [reddit-viewer.reddit :refer [display-posts]]
     [reddit-viewer.chart :as chart]
     [reddit-viewer.events]
     [reddit-viewer.subs]
     [reddit-viewer.views :refer [navbar]]
     [re-frame.core :as rf]))

(defn home-page []
  (let [view (rf/subscribe [:view])]
    (fn [] 
      [:div
       [navbar view]
       [:div.card>div.card-block {:style {:text-align "center"
                                          :margin "auto"
                                          :padding "auto"
                                          :padding-top "70px"}}
        (case @view
          :chart [:div {:style {:width "80vw" :height "90vh" :overflow "hidden"}} [chart/chart-posts-by-votes]]
          :posts [display-posts @(rf/subscribe [:posts])])]])))


(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn ^:export init! []
  (rf/dispatch-sync [:initialize-db])
  (rf/dispatch [:load-posts "Aww" 30])
  (mount-root))