(ns aoc)

(def input-lines
  (->>
    (slurp "inputs/1.txt")
    clojure.string/split-lines
    (map #(Integer/parseInt %))
  ))
