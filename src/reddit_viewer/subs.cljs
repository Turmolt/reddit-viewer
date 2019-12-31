(ns reddit-viewer.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :view
 (fn [db _]
   (:view db)))

(rf/reg-sub
 :posts
 (fn [db _]
   (:posts db)))

(rf/reg-sub
 :subreddit
 (fn [db _]
   (:subreddit db)))

(rf/reg-sub
 :post-count
 (fn [db _]
   (:post-count db)))

(rf/reg-sub
 :search-val
 (fn [db _]
   (:search-val db)))