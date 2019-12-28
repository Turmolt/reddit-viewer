(ns reddit-viewer.chart
  (:require ["chart.js" :as chartjs]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn render-data-chart [node data]
  (chartjs/Chart.
   node
   (clj->js
    {:type    "bar"
     :data    {:labels    (map :title data)
               :datasets  [{:label "votes"
                            :data  (map :score data)
                            :backgroundColor "rgba(0,0,255,.5)"}
                           {:label "comments"
                            :data (map :num_comments data)
                            :backgroundColor "rgba(200,200,255,.5)"}]}
     :options {:scales {:xAxes [{:display false}]}}})))

(defn destroy-chart [chart]
  (when @chart
    (.destroy @chart)
    (reset! chart nil)))

(defn render-chart [chart]
  (fn [component]
    (when-let [posts @(rf/subscribe [:posts])]
      (destroy-chart chart)
      (reset! chart (render-data-chart (r/dom-node component) posts)))))

(defn render-canvas []
  (when @(rf/subscribe [:posts]) [:canvas]))

(defn chart-posts-by-votes []
  (let [chart (r/atom nil)]
    (r/create-class
     {:component-did-mount (render-chart chart)
      :component-did-update (render-chart chart)
      :component-will-unmount (fn [_] (destroy-chart chart))
      :render                 render-canvas})))


