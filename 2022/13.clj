(ns aoc
  (:require [clojure.string :as str])
  (:require [clojure.set :as set])
  (:require [clojure.zip :as zip])
  (:require [clojure.math.combinatorics :as combo])
  (:require [clojure.walk :as walk]))

(def lines (str/split-lines (slurp "inputs/13.txt")))

(def test1 '("[1,1,3,1,1]"
             "[1,1,5,1,1]"
             ""
             "[[1],[2,3,4]]"
             "[[1],4]"
             ""
             "[9]"
             "[[8,7,6]]"
             ""
             "[[4,4],4,4]"
             "[[4,4],4,4,4]"
             ""
             "[7,7,7,7]"
             "[7,7,7]"
             ""
             "[]"
             "[3]"
             ""
             "[[[]]]"
             "[[]]"
             ""
             "[1,[2,[3,[4,[5,6,7]]]],8,9]"
             "[1,[2,[3,[4,[5,6,0]]]],8,9]"))

(defn pairs [lines]
  (as-> lines ls
    (map #(str/replace % #"," " ") ls)
    (partition 2 3 ls)
    (map (fn [pair] (map read-string pair)) ls)))

(defn check-order [a b]
  (cond
    (and (number? a) (number? b) (< a b)) "true"
    (and (number? a) (number? b) (> a b)) "false"
    (and (number? a) (coll? b)) (check-order (list a) b)
    (and (coll? a) (number? b)) (check-order a (list b))
    (and (coll? a) (coll? b)) (or (some (partial apply check-order) (map vector a b))
                                  (cond
                                    (< (count a) (count b)) "true"
                                    (> (count a) (count b)) "false"))))

(defn part1 [lines]
  (->> lines
       (pairs)
       (map (partial apply check-order))
       (map parse-boolean)
       (map-indexed (fn [i p] [(inc i) p]))
       (filter (fn [[_ b]] b))
       (map first)
       (reduce +)))

(part1 test1)

(part1 lines)

; part2

(def divider1 [[2]])
(def divider2 [[6]])

(defn comp' [a b]
  (case (check-order a b)
    "true" -1
    "false" 1
    nil 0))

(defn part2 [lines]
  (let [packets (concat (list divider1 divider2) (apply concat (pairs lines)))
        sorted (sort-by identity comp' packets)
        i1 (inc (.indexOf sorted divider1))
        i2 (inc (.indexOf sorted divider2))]
    (* i1 i2)))

(part2 test1)

(part2 lines)
