(ns aoc
  (:require [clojure.string :as str]))

(def lines (str/split-lines (slurp "inputs/02.txt")))
(def pairs (map #(str/split % #" ") lines))

(first pairs)


(defn score-throw [throw]
  (case throw
    "X" 1
    "Y" 2
    "Z" 3))

(defn score-outcome [theirs mine]
  (cond
    (and (= mine "X") (= theirs "C")) 6
    (and (= mine "X") (= theirs "A")) 3
    (and (= mine "Y") (= theirs "A")) 6
    (and (= mine "Y") (= theirs "B")) 3
    (and (= mine "Z") (= theirs "B")) 6
    (and (= mine "Z") (= theirs "C")) 3
    :else 0))


(defn score-round [pair]
  (let [[theirs mine] pair]
    (+ (score-throw mine) (score-outcome theirs mine))))

(score-throw "Y")
(score-outcome "A" "Y")
(score-round ["A" "Y"])

(def test-pairs [["A" "Y"] ["B" "X"] ["C" "Z"]])
(reduce + (map score-round test-pairs))

(reduce + (map score-round pairs))


; X = lose
; Y = draw
; Z = win
(defn get-throw [pair]
  (let [[theirs outcome] pair]
    (cond
      (and (= theirs "A") (= outcome "X")) "Z"
      (and (= theirs "A") (= outcome "Y")) "X"
      (and (= theirs "A") (= outcome "Z")) "Y"
      (and (= theirs "B") (= outcome "X")) "X"
      (and (= theirs "B") (= outcome "Y")) "Y"
      (and (= theirs "B") (= outcome "Z")) "Z"
      (and (= theirs "C") (= outcome "X")) "Y"
      (and (= theirs "C") (= outcome "Y")) "Z"
      (and (= theirs "C") (= outcome "Z")) "X")))

(defn score-round2 [pair]
  (let [[theirs outcome] pair
        mine (get-throw pair)]
    (+ (score-throw mine) (score-outcome theirs mine))))

(reduce + (map score-round2 pairs))
