(ns aoc)

(def lines (clojure.string/split-lines (slurp "inputs/input1.txt")))
(def input (map #(eval (read-string %)) lines))

(reduce + input)
