(ns aoc
  (:require [clojure.string :as string])
  )

(defn foo []
  (println "bar")
  )

(defn get-string-lines [fname]
  (string/split-lines (slurp (format "inputs/%s" fname)))
  )

(defn make-ints [things]
  (map #(Integer. %) things)
  )
