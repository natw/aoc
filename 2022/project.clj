(defproject aoc "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/math.combinatorics "0.1.6"]]
  :main ^:skip-aot aoc
  :target-path "target/%s"
  :plugins [[cider/cider-nrepl "0.28.5"]]
  :jvm-opts ["-XX:+ShowCodeDetailsInExceptionMessages"]
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
