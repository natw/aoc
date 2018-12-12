(ns aoc)

(def serial 5034)

(defn hundreds-digit [num]
  (mod (int (Math/floor (/ num 100))) 10))

(hundreds-digit 945) ; 9
(hundreds-digit 1234) ; 2

(defn power-level' [serial x y]
  (let [rack-id (+ x 10)]
    (- (hundreds-digit (* rack-id (+ (* rack-id y) serial))) 5)))
(def power-level (memoize power-level'))

(power-level 57 122 79) ; -5
(power-level 39 217 196) ; 0
(power-level 71 101 153) ; 4


(defn product [colls]
  (if (empty? colls)
    '(())
    (for [x (first colls)
          more (product (rest colls))]
      (cons x more))))
(product [[1 2] [3 4]])

(def grid-xs (range 0 300))
(def grid-ys (range 0 300))
(def grid-coords (product [grid-xs grid-ys]))

(defn andy [x y] (range x (+ x y)))

(defn square-power' [serial size startx starty]
  (let [coords (product [(andy startx size) (andy starty size)])
        pwr-lvl (partial power-level serial)]
    (reduce + 0 (map #(apply pwr-lvl %) coords))))

(vector 1 2)

(defn outer-power-level-sum [serial size startx starty]
  (let [right-side (map #(vector (+ startx (dec size)) %) (range starty (+ starty size)))
        bottom (map #(vector % (+ starty (dec size))) (range startx (dec (+ startx size))))]
    (reduce + (map #(apply (partial power-level serial) %) (concat right-side bottom)))))

(outer-power-level-sum 18 2 33 45) ; 10
(outer-power-level-sum 18 3 33 45) ; 15
(outer-power-level-sum 42 2 21 61) ; 17
(outer-power-level-sum 42 3 21 61) ; 17
  
(defn square-power2 [serial size startx starty]
  (if (= 1 size)
    (power-level serial startx starty)
    (let [outer (outer-power-level-sum serial size startx starty)
          inner (square-power serial (dec size) startx starty)]
      (+ outer inner)
      )
    ))

(def square-power (memoize square-power2))



(defn find-highest [serial sizes]
  (let [coords (product [grid-xs grid-ys])
        coord-sizes (product [coords sizes])]
    (apply max-key (fn [[cs size] & css]
                     (let [[x y] cs]
                       (square-power serial size x y))) coord-sizes)))



(find-highest serial (range 3 4))
; :(
