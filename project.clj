(defproject logistics "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler logistics.core/handler}
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [liberator "0.12.2"]
                 [compojure "1.2.1"]
                 [ring/ring-core "1.3.1"]
                 [aysylu/loom "0.5.0"]
                 [org.clojure/data.json "0.2.5"]]
  :main logistics.core
  :profiles {:uberjar {:aot :all}})
