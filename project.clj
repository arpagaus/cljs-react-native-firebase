(defproject mcumuluz-fetcher "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "3.7.0"]
                 [ring/ring-codec "1.0.1"]
                 [clojure.java-time "0.3.0"]
                 [clj-tagsoup/clj-tagsoup "0.3.0"]
                 [cheshire "5.8.0"]
                 [proto-repl "0.3.1"]
                 [com.ashafa/clutch "0.4.0"]]
  :plugins [[lein-cljfmt "0.5.7"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :jvm-opts ["-XX:-OmitStackTraceInFastThrow"])
