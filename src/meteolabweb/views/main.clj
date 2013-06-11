(ns meteolabweb.views.main
  (:require
   [noir.response :as response]
   [meteolabweb.views.common :as common])
  (:use [noir.core :only [defpage defpartial]]
        [hiccup.core :only [html]]
        [hiccup.page-helpers :only [include-css include-js html5]]
        [clojure.data.json :only [read-json json-str]]
        [clojure.set :only [union]]
        [meteolab.cdm :only [catalog-vars]]))

(def var-list
  (sort
   (apply union
          (map catalog-vars
               (map last common/catalogs)))))

;; refactor to use enlive
(defpage "/timeseries" []
  (html5
   [:head
    [:title "meteolabweb"]
    (include-css "http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css")
    (include-js "http://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js")
    (include-js "http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js")
    (include-js "/js/timeseries.js")
    (include-css "/css/reset.css")
    (include-js "/js/dygraph.js")]
   [:body
    [:table
     [:tr
      [:td "variable"]
      [:td "latitude"]
      [:td "longitude"]
      [:td "level"]
      [:td "units"]]
     [:tr
      [:td [:input {:id "vari"}]]
      [:td [:input {:id "lat"}]]
      [:td [:input {:id "lon"}]]
      [:td [:input {:id "level"}]]
      [:td [:select
            [:option {:value "Metric"} "Metric"]
            [:option {:value "English"} "English"]]]
      [:td [:button {:type "button" :id "submit"} "GO!"]]]]
    [:div#loader [:img {:src "/img/ajax-loader.gif"}]]
    [:div#title]
    [:div#subtitle]
    [:div#graphdiv {:style "width:1000px; height:300px;"}]
    [:div#status {:style "width:1000px; height:300px;"}]]))

(defpage "/autocomp" []
  (response/json var-list))
