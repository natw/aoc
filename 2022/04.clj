(ns aoc
  (:require [clojure.string :as str])
  (:require [clojure.set :as set]))

(def lines (str/split-lines (slurp "inputs/04.txt")))

(def test1 '("2-4,6-8"
             "2-3,4-5"
             "5-7,7-9"
             "2-8,3-7"
             "6-6,4-6"
             "2-6,4-8"))

(defn parse-int [x]
  (Integer/parseInt x))

(defn parse-range [rs]
  (let [[a b] (map parse-int (str/split rs #"-"))]
    (set (range a (+ 1 b)))))

(defn parse-line [line]
  (map parse-range (str/split line #",")))

(defn parse-lines [lines]
  (map parse-line lines))

(first (parse-lines test1))

; true if either range fully contains the other
(defn has-containment [[r1 r2]]
  (let [i (set/intersection r1 r2)]
    (or (= r1 i) (= r2 i))))

(count (filter has-containment (parse-lines test1)))

(count (filter has-containment (parse-lines lines)))

; part 2

(defn has-overlap? [[r1 r2]]
  (seq (set/intersection r1 r2)))

(count (filter has-overlap? (parse-lines test1)))
(count (filter has-overlap? (parse-lines lines)))
