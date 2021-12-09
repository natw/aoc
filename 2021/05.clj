(ns aoc
  (:require [clojure.string :as string]))

(def test-raw "
0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2
  ")

(def raw-input (slurp "inputs/5"))

(defn parse-line [line]
  (map #(Integer. %) (mapcat #(string/split % #",")
                             (string/split line #" -> "))))

(parse-line "1,2 -> 3,4")

(defn parse-lines [raw-input]
  (->> (string/split-lines raw-input)
       (map string/trim)
       (remove empty?)
       (map parse-line)))

(parse-lines test-raw)

(defn absrange [a b]
  (range (min a b) (inc (max a b))))

(defn get-points [line]
  (let [[x1 y1 x2 y2] line]
    (for [x (absrange x1 x2)
          y (absrange y1 y2)
          :when (or (= x1 x2) (= y1 y2))]
      (list x y))))

(defn part1 [input]
  (let [points (mapcat get-points (parse-lines input))
        counts (frequencies points)]
    (count (filter #(<= 2 (get counts %)) (keys counts)))))
(part1 test-raw)

(part1 raw-input)

(defn abs [a]
  (max a (- a)))

(defn diagonal [points]
  (let [[x1 y1 x2 y2] points]
    (= (abs (- x1 x2))
       (abs (- y1 y2)))))

(defn get-points' [line]
  (let [[x1 y1 x2 y2] line]
    (for [x (absrange x1 x2)
          y (absrange y1 y2)
          :when (or (= x1 x2)
                    (= y1 y2)
                    (diagonal (list x1 y1 x y)))]

      (list x y))))

(get-points' '(0 0 2 2))
(diagonal '(0 1 2 2))
(defn part2 [input]
  (let [points (mapcat get-points' (parse-lines input))
        counts (frequencies points)]
    (count (filter #(<= 2 (get counts %)) (keys counts)))))

(part2 test-raw)
(part2 raw-input)
