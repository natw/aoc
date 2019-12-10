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

(defn fuel-required'
  ([mass] (fuel-required' 0 mass))
  ([total mass]
   (let [req (fuel-required mass)]
     (if (<= req 0)
       total
       (fuel-required' (+ total req) req))
     )
   )
  )

(fuel-required' 14)

(reduce + (map fuel-required' input-lines))
