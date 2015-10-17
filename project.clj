(defproject bowling "0.1.0"
  :description "Bowling score keeper, used as playground for learning clojure."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]]
  :main ^:skip-aot bowling.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
