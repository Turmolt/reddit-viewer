(ns reddit-viewer.reddit
  (:require [re-frame.core :as rf]))

(defn trim-title [title] (str (subs title 0 180) "..."))

(defn display-post [{:keys [permalink subreddit title score url]}]
  [:div.card.m-2 {:style {:width 400 
                          :height 550}}
   [:div.card-block
    [:h4.card-title {:style {:font-size 18 :padding-top 5}}
     [:a {:href (str "http://www.reddit.com" permalink)} (trim-title title) " "]]
    [:div [:span.badge.badge-info {:color "info"} subreddit " : " score]]
    [:img {:width "300px" :height "440px" :src url :style {:object-fit "scale-down"}}]]])

(defn display-posts [posts]
  (if-not (empty? posts)
    [:div
     (for [posts-row (partition-all 3 posts)]
       ^{:key posts-row}
       [:div.row
        (for [post posts-row]
          ^{::key post}
          [display-post post])])]
    [:div {:style {:padding 100}}[:h (str "Loading...")]]))

(defn sort-posts [title sort-key]
  [:button.btn.btn-secondary
   {:on-click #(rf/dispatch [:sort-posts sort-key])}
   (str title)])