(ns aoc
  (:require [clojure.string :as str])
  (:require [clojure.set :as set]))

(def lines (str/split-lines (slurp "inputs/06.txt")))
(def input (first lines))

(def test1 "bvwbjplbgvbhsrlpgdmjqwftvncz")
(def test2 "nppdvjthqldpwncqszvftbrmjlhg")
(def test3 "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")
(def test4 "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")

(defn part1 [signal]
  (let [iwindows (map-indexed (fn [i,s] (list (+ i 4) s)) (partition 4 1 signal))
        all-uniq (fn [[i s]] (= (count s) (count (distinct s))))]
     (first (first (filter all-uniq iwindows)))))

(part1 test1) ; 5
(part1 test2) ; 6
(part1 test3) ; 10
(part1 test4) ; 11

(part1 input)

(defn part2 [signal]
  (let [iwindows (map-indexed (fn [i,s] (list (+ i 14) s)) (partition 14 1 signal))
        all-uniq (fn [[i s]] (= (count s) (count (distinct s))))]
     (first (first (filter all-uniq iwindows)))))

(part2 test1)
(part2 test2)

(part2 input)
