(defproject mcumuluz-fetcher "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "3.7.0"]
                 [ring/ring-codec "1.0.1"]
                 [clojure.java-time "0.3.0"]
                 [clj-tagsoup/clj-tagsoup "0.3.0"]
                 [cheshire "5.8.0"]
                 [proto-repl "0.3.1"]
                 [com.ashafa/clutch "0.4.0"]
                 [proto-repl "0.3.1"]
                 [com.squareup.okhttp/okhttp "2.5.0"]
                 [happy "0.5.2"]]
  :plugins [[lein-cljfmt "0.5.7"]
            [lein-auto "0.1.3"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :jvm-opts ["-XX:-OmitStackTraceInFastThrow"])
