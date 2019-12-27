(ns reddit-viewer.reddit
  (:require [ajax.core :as ajax]
            [reagent.core :as r]))

(defonce posts (r/atom nil))

(defn find-posts-with-image [posts]
  (filter #(= (:post_hint %) "image") posts))


(defn load-posts [subreddit n]
  (ajax/GET (str "https://www.reddit.com/r/" subreddit ".json?sort=new&limit=" n)
    {:handler                   #(->> (get-in % [:data :children])
                                      (map :data)
                                      (find-posts-with-image)
                                      (reset! posts))
     :response-format :json
     :keywords? true}))


(defn display-post [{:keys [permalink subreddit title score url]}]
  [:div.card.m-2 {:style {:width 400
                          :height 550}}
   [:div.card-block
    [:h4.card-title {:style {:font-size 20}}
     [:a {:href (str "http://www.reddit.com" permalink)} title " "]]
    [:div [:span.badge.badge-info {:color "info"} subreddit " : " score]]
    [:img {:width "300px" :src url}]]])

(defn display-posts [posts]
  (if-not (empty? posts)
    [:div
     (for [posts-row (partition-all 3 posts)]
       ^{:key posts-row}
       [:div.row
        (for [post posts-row]
          ^{::key post}
          [display-post post])])]
    [:div [:h (str "Loading...")][:br] [:br]]))

(defn sort-posts [title sort-key]
  (when-not (empty? @posts)
    [:button.btn.btn-secondary
     {:on-click #(swap! posts (partial sort-by sort-key >))}
     (str title)]))

(load-posts "Aww" 30)