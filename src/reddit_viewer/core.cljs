(ns reddit-viewer.core
    (:require
     [reagent.core :as r]
     [reddit-viewer.reddit :refer [display-posts posts sort-posts load-posts]]
     [reddit-viewer.chart :as chart]))

;; -------------------------
;; Views

(defn navitem [title view id]
  [:li.nav-item
   {:class-name (when (= id @view) "active")}
   [:a.nav-link
    {:href     "#"
     :on-click #(reset! view id)}
    title]])

(defn search-bar []
  (let [subreddit (r/atom "Unity3D")
        n (r/atom 30)]
    [:form.form-inline.my-2.my-lg-0
     [:select
      {:class "form-control"
       :id "numberPosts"
       :on-change #(reset! n (-> % .-target .-value))}
      [:option 30] [:option 50] [:option 100]]
     [:div {:style {:padding-right 5}}]
     [:input.form-control.mr-sm-2
      {:type "Search"
       :placeholder "Aww"
       :aria-label "Search"
       :on-change #(reset! subreddit (-> % .-target .-value))}]
     [:button.btn.btn-outline-success.my-2.my-sm-0
      {:type "submit"
       :on-click #(load-posts @subreddit @n)} "Enter"]]))

(defn navbar [view]
  [:nav.navbar.navbar-expand-lg.fixed-top.navbar-dark.bg-dark
   [:ul.navbar-nav.mr-auto.nav
    {:className "navbar-nav mr-auto"}
    [navitem "Posts" view :posts]
    [navitem "Chart" view :chart]]
   [:div {:style {:padding-right 5 :color "white"}}"sort by "]
   [:div.btn-group
    {:style {:padding-right 5}}
    [sort-posts "score" :score]
    [sort-posts "comments" :num_comments]]
   [search-bar]])

(defn home-page []
  (let [view (r/atom :posts)]
    (fn [] 
      [:div
       [navbar view]
       [:div.card>div.card-block {:style {:text-align "center"
                                          :margin "auto"
                                          :padding "auto"
                                          :padding-top "70px"}}
        (case @view
          :chart [:div {:style {:width 1000}} [chart/chart-posts-by-votes posts]]
          :posts [display-posts @posts])]])))

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
