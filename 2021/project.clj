(defproject aoc "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.10.3"]]
  :main ^:skip-aot aoc.core
  :target-path "target/%s"
  :plugins [[cider/cider-nrepl "0.26.0"]]
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
