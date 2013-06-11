(defproject meteolabweb "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [noir "1.2.1"]
                 [meteolab/meteolab-data "1.0.0-SNAPSHOT"]
                 [org.clojure/data.json "0.1.2"]]
  :main meteolabweb.server
  :repositories {"project.local"  ~(-> "repo" java.io.File. .toURL str)})
