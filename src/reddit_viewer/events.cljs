(ns reddit-viewer.events
  (:require [ajax.core :as ajax]
            [re-frame.core :as rf]))

(rf/reg-event-db
 :initialize-db
 (fn [_ _]
   {:view      :posts
    :sort-key  :score
    :subreddit "Aww"
    :search-val "Aww"
    :post-count 30}))

(defn find-posts-with-preview [posts]
  (filter #(not (= (:post_hint %) "image")) posts))

(def trim-event
  (rf/->interceptor
   :id          :trim-event
   :before      (fn [context]
                  (let [trim-fn (fn [event] (-> event rest vec))]
                    (update-in context [:coeffects :event] trim-fn)))))

(rf/reg-event-db
 :set-posts
 [trim-event]
 (fn [db [posts]]
   (assoc db :posts
          (->> (get-in posts [:data :children])
               (map :data)))))

(rf/reg-event-db
 :set-subreddit
 [trim-event]
 (fn [db [subreddit]]
   (assoc db :subreddit subreddit)))

(rf/reg-event-db
 :set-post-count
 [trim-event]
 (fn [db [n]]
   (assoc db :post-count n)))

(rf/reg-event-db
 :set-search-val
 [trim-event]
 (fn [db [search-val]]
   (assoc db :search-val search-val)))

(rf/reg-fx
 :ajax-get
 (fn [[url handler]]
   (ajax/GET url
    {:handler 			handler
     :response-format 	:json
     :keywords?			true})))

(rf/reg-event-fx
 :load-posts
 (fn [_ [_ subreddit n]]
   {:ajax-get [(str "https://www.reddit.com/r/" subreddit ".json?sort=new&limit=" n) 
               #(rf/dispatch [:set-posts %])]}))

(rf/reg-event-db
 :sort-posts
 [trim-event]
 (fn [db [sort-key]]
   (update db :posts (partial sort-by sort-key >))))

(rf/reg-event-db
 :select-view
 [trim-event]
 (fn [db [view]]
   (assoc db :view view)))