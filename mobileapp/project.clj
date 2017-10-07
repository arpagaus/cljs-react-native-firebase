(defproject mobileapp "0.1.0-SNAPSHOT"
            :description "FIXME: write description"
            :url "http://example.com/FIXME"
            :license {:name "Eclipse Public License"
                      :url  "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.9.0-alpha16"]
                           [org.clojure/clojurescript "1.9.854"]
                           [reagent "0.7.0" :exclusions [cljsjs/react cljsjs/react-dom cljsjs/react-dom-server cljsjs/create-react-class]]
                           [re-frame "0.9.3"]
                           [react-native-externs "0.1.0"]
                           [com.andrewmcveigh/cljs-time "0.5.0"]
                           [happy "0.5.2"]]
            :plugins [[lein-cljsbuild "1.1.4"]
                      [lein-figwheel "0.5.11"]
                      [lein-cljfmt "0.5.7"]
                      [lein-doo "0.1.8"]]
            :clean-targets ["target/" "main.js"]
            :aliases {"figwheel"        ["run" "-m" "user" "--figwheel"]
                      ; TODO: Remove custom extern inference as it's unreliable
                      ;"externs"         ["do" "clean"
                      ;                   ["run" "-m" "externs"]]
                      "rebuild-modules" ["run" "-m" "user" "--rebuild-modules"]
                      "prod-build"      ^{:doc "Recompile code with prod profile."}
                                        ["with-profile" "prod" "cljsbuild" "once" "main"]}
            :profiles {:dev  {:dependencies [[figwheel-sidecar "0.5.10"]
                                             [com.cemerick/piggieback "0.2.1"]]
                              :source-paths ["src" "env/dev"]
                              :cljsbuild    {:builds [{:id           "main"
                                                       :source-paths ["src" "env/dev"]
                                                       :figwheel     true
                                                       :compiler     {:output-to     "target/not-used.js"
                                                                      :main          "env.main"
                                                                      :output-dir    "target"
                                                                      :optimizations :none}}
                                                      {:id           "test"
                                                                               :source-paths ["src" "test"]
                                                                               :compiler     {:output-to     "target/testable.js"
                                                                                              :main          "env.main"
                                                                                              :output-dir    "target"
                                                                                              :optimizations :advanced}}]}
                              :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}
                       :prod {:cljsbuild {:builds [{:id           "main"
                                                    :source-paths ["src" "env/prod"]
                                                    :compiler     {:output-to          "main.js"
                                                                   :main               "env.main"
                                                                   :output-dir         "target"
                                                                   :static-fns         true
                                                                   :externs            ["js/externs.js"]
                                                                   :infer-externs      true
                                                                   :parallel-build     true
                                                                   :optimize-constants true
                                                                   :optimizations      :advanced
                                                                   :closure-defines    {"goog.DEBUG" false}}}]}}})
