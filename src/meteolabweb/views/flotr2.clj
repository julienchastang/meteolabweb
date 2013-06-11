(ns meteolabweb.views.flotr2
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
(defpage "/flotr2" []
  (html5
   [:head
    [:title "meteolabweb"]
    (include-css "http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css")
    (include-js "http://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js")
    (include-js "http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js")
    (include-css "/css/reset.css")
    [:body
     [:div#container {:style "width:1000px; height:300px;"}]
     (include-js "/js/flotr2.min.js")
     (include-js "/js/flotrtest.js")]]))
