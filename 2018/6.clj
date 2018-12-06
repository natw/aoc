(ns aoc)

(def input-lines (clojure.string/split-lines (slurp "inputs/6.txt")))

; seq of vector pairs
(def input (map (fn [pair] (vec (map #(Integer. %) pair))) (map #(clojure.string/split % #", ") input-lines)))

(def xbounds 
  (let [xs (map first input)]
    (vector (apply min xs) (apply max xs))
  ))
(def ybounds 
  (let [ys (map second input)]
    (vector (apply min ys) (apply max ys))
  ))

(defn abs [n] (max n (-' n)))

(defn distance [a b]
  (let [[xa ya] a [xb yb] b]
    (+ (abs (- xa xb))
       (abs (- ya yb)))))

(= 5 (distance [1 1] [3 4]))

(defn distance-from [coords]
  (partial distance coords))

(defn closest-location [locations point]
  (apply (partial min-key (distance-from point)) locations))

(= [2 2] (closest-location '([2 2] [10 10]) [1 1]))

(defn touches-infinite? [locations location]
  (let [[x y] location]
    (or (= location (closest-location locations [(- x 1000) y]))
        (= location (closest-location locations [(+ x 1000) y]))
        (= location (closest-location locations [x (- y 1000)]))
        (= location (closest-location locations [x (+ y 1000)])))) )

(def test-points '([1 1] [1 6] [8 3] [3 4] [5 5] [8 9]))

(true? (touches-infinite? test-points [1 1]))
(true? (touches-infinite? test-points [8 9]))
(true? (touches-infinite? test-points [5 5]))

(closest-location test-points [5 -1000])

(distance [5 -100] [5 5])
(distance [5 -100] [1 6])

