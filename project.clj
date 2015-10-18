(defproject bowling "0.1.0"
  :description "Bowling score keeper, used as playground for learning clojure."
  :url "http://example.com/FIXME"
  :license {:name "Apache License 2.0"
            :url "http://www.apache.org/licenses/"}
  :dependencies [[org.clojure/clojure "1.7.0"]]
  :main ^:skip-aot bowling.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
