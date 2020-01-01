(ns reddit-viewer.reddit
  (:require [re-frame.core :as rf]))

(defn trim-title [title] (str (subs title 0 180) (if (> (count title) 180) "..." "")))

(defn display-post [{:keys [permalink subreddit title score url post_hint thumbnail]}]
  [:a {:href (str "http://www.reddit.com" permalink) :height 100}
   [:div.card.m-2 {:style {:width 400
                           :height 550
                           :padding 10}}
    [:h4.card-title {:style {:font-size 18 :padding-top 5}} (trim-title title) " "]
    [:div.card-block {:style {:height 500
                              :display "flex"
                              :flex-direction "column"
                              :justify-content "center"
                              :align-items "center"}}
     (case post_hint
       "image" [:img {:width "300px" :height "400px" :src url :style {:object-fit "scale-down" :margin "auto"}}]
       (if (some #(= % thumbnail) ["self" "default"])
         [:div {:style {:font-size  30}} (str thumbnail " post")]
         [:div {:style {:width "300px" :margin "auto"}}
          [:img {:src thumbnail
                 :width 250
                 :style {:object-fit "scale-up"}}]
          [:br] [:p (str post_hint)]]))
     [:div [:span.badge.badge-info {:color "info"} subreddit " : " score]]]]])

(defn display-posts [posts]
  (if-not (empty? posts)
    [:div
     (for [posts-row (partition-all 3 posts)]
       ^{:key posts-row}
       [:div.row
        (for [post posts-row]
          ^{::key post}
          [display-post post])])]
    [:div {:style {:padding 100}}
     [:img {:src "/reddit-viewer/public/resources/BackwardsCaptainLogo.png"
            :width 100 :height 100
            :style {:margin "auto"
                    :animation "rotation 6s infinite linear"}}]]))

(defn sort-posts [title sort-key]
  [:button.btn.btn-secondary
   {:on-click #(rf/dispatch [:sort-posts sort-key])}
   (str title)])
