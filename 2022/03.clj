(ns aoc
  (:require [clojure.string :as str])
  (:require [clojure.set :as set]))

(def lines (str/split-lines (slurp "inputs/03.txt")))

(def test1 '("vJrwpWtwJgWrhcsFMMfFFhFp"
             "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL"
             "PmmdzqPrVvPwwTWBwg"
             "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn"
             "ttgJtRGJQctTZtZT"
             "CrZsJsPPZsGzwwsLwLmpwMDw"))

(defn split-line [line]
  (let [half (/ (count line) 2)]
       (list (take half line) (take-last half line))))

(split-line "abc123")

(set/difference #{\a \b} #{\a \b \c})

(defn priority [item]
  (let [cval (int item)]
    (if (< cval 97)
      (- cval 38)
      (- cval 96))))

(defn score-line [line]
  (let [[c1 c2] (split-line line)
        in-both (set/intersection (set c1) (set c2))]
    (reduce + (map priority in-both))))

(defn score-all [lines]
  (reduce + (map score-line lines)))

(score-all test1)

(score-all lines)

; part 2


(def groups (partition 3 lines))
(def test-groups (map seq (partition 3 test1)))
(first test-groups)

(defn badge [group]
  (let [sacks (map set (map seq group))]
    (first (apply set/intersection sacks))))

(badge (first test-groups))

(->> test-groups
     (map badge)
     (map priority)
     (reduce +))

(->> groups
     (map badge)
     (map priority)
     (reduce +))
