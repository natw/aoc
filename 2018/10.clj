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
  
(def grid-xs (range 0 300))
(def grid-ys (range 0 300))

(defn product [colls]
  (if (empty? colls)
    '(())
    (for [x (first colls)
          more (product (rest colls))]
      (cons x more))))
(product [[1 2] [3 4]])

(defn square-power [serial size startx starty]
  (let [coords (product [(range startx (+ startx size)) (range starty (+ starty size))])
        pwr-lvl (partial power-level serial)]
    (reduce + 0 (map #(apply pwr-lvl %) coords))))

(square-power 21 61 42) ; 30


(def coords (product [grid-xs grid-ys]))

(defn find-highest [serial]
  (let [coords (product [grid-xs grid-ys])
        coord-sizes (product [coords (range 1 301)])]
    (apply max-key (fn [[cs size] & css]
                     (let [[x y] cs]
                       (square-power serial size x y))) coord-sizes)))


(find-highest serial)
; :(
