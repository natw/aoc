(ns aoc)

(def input-lines
  (->>
    (slurp "inputs/1.txt")
    clojure.string/split-lines
    (map #(Integer/parseInt %))
  ))

(first input-lines)

(defn fuel-required [mass]
  (- (quot mass 3) 2))

(fuel-required 1969)
(fuel-required 100756)

(reduce + (map fuel-required input-lines))
