(ns aoc
  (:require [clojure.string :as string]))

(defn split-input [i]
  (map #(Integer. (string/trim %)) (string/split i #",")))

(def test1 (split-input "16,1,2,0,4,2,7,1,2,14"))

(def input1 (split-input (slurp "inputs/7")))

(defn diff [a b]
  (max (- a b) (- b a)))

(defn alignment-cost [crabs position]
  (reduce + (map #(diff position %) crabs)))

(alignment-cost test1 2)

(defn part1 [crabs]
  (apply min
         (map
          #(alignment-cost crabs %)
          (range (apply min crabs) (+ (apply max crabs) 1)))))

(part1 test1) ; 37

(part1 input1)

(defn movement-cost [a b]
  (let [n (diff a b)]
    (/ (* n (+ n 1)) 2)))

(defn alignment-cost' [crabs position]
  (reduce + (map #(movement-cost position %) crabs)))
(defn part2 [crabs]
  (apply min
         (map
          #(alignment-cost' crabs %)
          (range (apply min crabs) (+ (apply max crabs) 1)))))

(part2 input1) ; 99266250
