(ns reddit-viewer.views
  (:require [re-frame.core :as rf]
            [reddit-viewer.subs]
            [reddit-viewer.events]
            [reddit-viewer.reddit :refer [sort-posts]]))

(defn navitem [title view id]
  [:li.nav-item
   {:class-name (when (= id @view) "active")}
   [:a.nav-link
    {:href     "#"
     :on-click #(rf/dispatch [:select-view id])}
    title]])

(defn search-bar []
  (let [subreddit @(rf/subscribe [:subreddit])
        n @(rf/subscribe [:post-count])
        search-val @(rf/subscribe [:search-val])]
    [:form.form-inline.my-2.my-lg-0
     [:select
      {:class "form-control"
       :id "numberPosts"
       :on-change #(rf/dispatch [:set-post-count  (-> % .-target .-value)])}
      [:option 30] [:option 50] [:option 100]]
     [:div {:style {:padding-right 5}}]
     [:input.form-control.mr-sm-2
      {:type "Search"
       :placeholder "Aww"
       :aria-label "Search"
       :on-change #(rf/dispatch [:set-search-val  (-> % .-target .-value)])
       :onSubmit (fn [e] (identity false))}]
     [:button.btn.btn-outline-success.my-2.my-sm-0
      {:type "button"
       :on-click #(do (if (not (= search-val subreddit))
                        (do (rf/dispatch [:set-subreddit search-val])
                            (rf/dispatch [:set-posts nil])))
                      (rf/dispatch [:load-posts search-val n]))} "Enter"]]))

(defn navbar [view]
  [:nav.navbar.navbar-expand-lg.fixed-top.navbar-dark.bg-dark
   [:ul.navbar-nav.mr-auto.nav
    {:className "navbar-nav mr-auto"}
    [:img {:src "/reddit-viewer/public/resources/BackwardsCaptainLogoLight.png"
           :width 30 :height 30 :style {:margin "auto"}}]
    [:div {:style {:width 10}}]
    [navitem "Posts" view :posts]
    [navitem "Chart" view :chart]]
   [:div {:style {:padding-right 5 :color "white"}} "sort by "]
   [:div.btn-group
    {:style {:padding-right 5}}
    [sort-posts "score" :score]
    [sort-posts "comments" :num_comments]]
   [search-bar]])