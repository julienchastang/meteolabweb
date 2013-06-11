(ns meteolabweb.views.timeseries
  (:require
   [noir.response :as response]
   [meteolabweb.views.common :as common])
  (:use [noir.core :only [defpage defpartial]]
        [hiccup.core :only [html]]
        [hiccup.page-helpers :only [include-css include-js html5]]
        [clojure.data.json :only [read-json json-str]]
        [clojure.set :only [union]]
        [meteolab.cdm
         :only [unit-convert time-series
                dataset-latest catalog-vars]]))


(defn ^:private separate [sep coll]
  (apply str
         (interpose sep (map str coll))))

(defn ^:private csv [ts ts-names]
  (letfn [(commify [c]
            (let [s (separate  \, c)]
              (str s \newline)))]
    (apply str
           (list*
            (str "Date," (commify ts-names))
            (for [x ts] (commify x))))))

;; 9.96921E36

(defn ^:private merge-with2 [f & maps]
  (apply
   (partial merge-with f)
   (map
    (fn [mymap] (reduce
                (fn [m k] (if (contains? m k)
                            m
                            (assoc m k nil)))
                mymap
                (reduce union (map #(-> % keys set) maps))))
    maps)))

(defn data [cats lat lon lvl id unit]
  (let [ts-coll (map (fn [[_ cat]]
                       (when-let [dsl (dataset-latest cat)]
                         (unit-convert
                          unit
                          (time-series dsl id [lat lon lvl]))))
                     cats)
        ts (into {} (some seq ts-coll))
        title (-> ts :data :desc)
        subtitle (separate \, (map ts [:lat :lon :z :z-unit]))
        yLabel (str (-> ts :data :name) " (" (-> ts :data :unit) ")" )
        names (map first cats)
        csv (csv (map flatten
                      (sort-by key
                               (apply
                                (partial merge-with2 vector)
                                (map #(apply zipmap %)
                                     (map #(vector (-> % :time) (-> % :data :vals))
                                          ts-coll)))))
                 names)]
    {:title title
     :subtitle subtitle
     :yLabel yLabel
     :csv csv}))

(defn junk [lat lon lvl id]
  (let [cat (-> common/catalogs first last)
        dsl (dataset-latest cat)
        ts (time-series dsl id [lat lon lvl])
        tst (sort-by first (map vector (-> ts :time) (-> ts :data :vals)))]
    tst))

(defpage [:get ["/ts/:v/:lat/:lon/:lvl" :v #".*" :lat #".*" :lon #".*" :lvl #".*"]] {:keys [v lat lon lvl]}
  (response/json
   (data common/catalogs (read-string lat) (read-string lon) (read-string lvl) v "Fahrenheit")))

(defpage [:get ["/flotrtest/:v/:lat/:lon/:lvl" :v #".*" :lat #".*" :lon #".*" :lvl #".*"]] {:keys [v lat lon lvl]}
  (response/json
   (junk (read-string lat) (read-string lon) (read-string lvl) v)))
