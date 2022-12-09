(ns aoc
  (:require [clojure.string :as str])
  (:require [clojure.set :as set])
  (:require [clojure.zip :as zip])
  (:require [clojure.walk :as walk]))

(def lines (str/split-lines (slurp "inputs/09.txt")))

(def test1 '("R 4"
             "U 4"
             "L 3"
             "D 1"
             "R 4"
             "D 1"
             "L 5"
             "R 2"))

(defn parse-int [s]
  (Integer/parseInt s))

(defn distance [[x1 y1] [x2 y2]]
  (max (abs (- x1 x2))
       (abs (- y1 y2))))

(defn move [start movement]
  (map + start movement))

(defn unitify [val]
  (cond
    (zero? val) 0
    :else (- (/ val (abs val)))))

(defn calc-diff-unit [xd yd]
  (cond
    (> (abs yd) (abs xd)) (list 0 (unitify yd))
    (> (abs xd) (abs yd)) (list (unitify xd) 0)
    (= (abs xd) (abs yd)) (list (unitify xd) (unitify yd))
    :else '(0 0)))

(calc-diff-unit -3 3)

; return new tail position that has caught up with the head
; in a sense, this doesn't really move the tail, but finds the position one step
; from the head in the direction of the tail, and returns that
(defn catch-up [head tail]
  (let [[xh yh] head
        [xt yt] tail
        xd (- xh xt)
        yd (- yh yt)
        diff-unit (calc-diff-unit xd yd)]
    (if (> (distance head tail) 1)
      (move (list xh yh) diff-unit)
      tail)))

(defn unit-multiplier [dir]
  (condp = dir
    \R '(1 0)
    \U '(0 1)
    \D '(0 -1)
    \L '(-1 0)))

; take a string command and generate the list of unit steps it describes
(defn cmd-to-moves [cmd]
  (let [dir (first cmd)
        length (parse-int (re-find #"\d+" cmd))]
    (repeat length (unit-multiplier dir))))

(defn part1 [lines]
  (let [moves (mapcat cmd-to-moves lines)]
    (loop [hpos '(0 0)
           tpos '(0 0)
           moves moves
           tvisited #{tpos}]
      (if (empty? moves)
        (count tvisited)
        (let [newh (move hpos (first moves))
              newt (catch-up newh tpos)]
          (recur newh newt (rest moves) (conj tvisited newt)))))))

(part1 test1)

(part1 lines)

(def test2 '("R 5"
             "U 8"
             "L 8"
             "D 3"
             "R 17"
             "D 10"
             "L 25"
             "U 20"))
(def test3 '("R 5"
             "U 8"))

(partition 2 1 '(1 2 3 4 5))

(reduce (fn [x y] (conj x (+ (last x) y))) (vector 1) '(1 2 3 4))

(defn move-chain [newh positions]
  (reduce (fn [acc p]
            (conj acc (catch-up (last acc) p)))
          (vector newh)
          (rest positions)))

(defn remap [f initial coll acc]
  (loop [item initial
         coll coll
         acc acc]
    (if (empty? coll)
      acc
      (recur (f item (first coll)) (rest coll) (conj acc item)))))


(defn part2 [lines]
  (let [moves (mapcat cmd-to-moves lines)]
    (loop [positions (repeat 10 '(0 0))
           moves moves
           tvisited #{(last positions)}]
      (if (empty? moves)
        (count tvisited)
        (let [newh (move (first positions) (first moves))
              newpositions (move-chain newh positions)]
          (recur newpositions (rest moves) (conj tvisited (last newpositions))))))))

(catch-up '(4 4) '(2 2))
(part2 test2)
; (part2 test3)

(part2 lines)
