(ns aoc.2022.15
  (:require [clojure.string :as str])
  (:require [clojure.set :as set])
  (:require [clojure.zip :as zip])
  (:require [clojure.math.combinatorics :as combo])
  (:require [clojure.walk :as walk]))

(def lines (str/split-lines (slurp "inputs/15.txt")))

(def test1 '("Sensor at x=2, y=18: closest beacon is at x=-2, y=15"
             "Sensor at x=9, y=16: closest beacon is at x=10, y=16"
             "Sensor at x=13, y=2: closest beacon is at x=15, y=3"
             "Sensor at x=12, y=14: closest beacon is at x=10, y=16"
             "Sensor at x=10, y=20: closest beacon is at x=10, y=16"
             "Sensor at x=14, y=17: closest beacon is at x=10, y=16"
             "Sensor at x=8, y=7: closest beacon is at x=2, y=10"
             "Sensor at x=2, y=0: closest beacon is at x=2, y=10"
             "Sensor at x=0, y=11: closest beacon is at x=2, y=10"
             "Sensor at x=20, y=14: closest beacon is at x=25, y=17"
             "Sensor at x=17, y=20: closest beacon is at x=21, y=22"
             "Sensor at x=16, y=7: closest beacon is at x=15, y=3"
             "Sensor at x=14, y=3: closest beacon is at x=15, y=3"
             "Sensor at x=20, y=1: closest beacon is at x=15, y=3"))

(defn parse-int [s]
  (Integer/parseInt s))

(defn find-nums [s]
  (mapv parse-int (re-seq #"-?\d+" s)))

(defn parse-lines [lines]
  (->> lines
       (map find-nums)))

(parse-lines test1)

(defn exclusions' [row [sx sy bx by]]
  (let [distance (+ (abs (- bx sx)) (abs (- by sy)))
        xwidth (- distance (abs (- sy row)))]
    (if (neg? xwidth)
      '()
      (for [x (range (- sx xwidth) (inc (+ sx xwidth)))
            :when (not (= [x row] [bx by]))]
        [x row]))))


(exclusions' 10 [8 7 2 10])

(defn part1' [lines row]
  (let [parsed (parse-lines lines)]
    (count (reduce set/union (map (fn [l] (into #{} (exclusions' row l))) parsed)))))

(part1' test1 10)

(part1' lines 2000000)
