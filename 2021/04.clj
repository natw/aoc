(ns aoc
  (:require [clojure.string :as string]))

(def t1 "7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

  22 13 17 1  0
   8  2 23  4 24
  21  9 14 16  7
   6 10  3 18  5
   1 12 20 15 19

   3 15  0  2 22
   9 18 13 17  5
  19  8  7 25 23
  20 11 10 24  4
  14 21 16 12  6

  14 21 17 24  4
  10 16 15  9 19
  18  8 23 26 20
  22 11 13  6  5
   2  0 12  3  7")

(defn split-input [input]
  (->
   input
   (string/replace #"[ ]{2,}" " ")
   (string/split #"\n\n")))

(def test1 (split-input t1))

(def input1 (split-input (slurp "inputs/4")))

(defn get-draws [lines]
  (map
   #(Integer. %)
   (-> lines
       (first)
       (string/split #","))))

(defn split2ints [line sep]
  (map #(Integer. %) (filter not-empty (string/split line sep))))

(defn get-boards [lines]
  (map #(split2ints % #"\s+") (rest lines)))

(def test-draws (get-draws (split-input t1)))
(def test-boards (get-boards (split-input t1)))

(defn column-values [board i]
  (take-nth 5 (nthrest board i)))

(defn column-bingo? [board called]
  (some true? (for [i (range 5)
                    :let [col (column-values board i)]]
                (every? #(.contains called %) col))))

(defn row-values [board i]
  (nth (partition 5 board) i))

(defn row-bingo? [board called]
  (some true? (for [i (range 5)
                    :let [row (row-values board i)]]
                (every? #(.contains called %) row))))

(defn diagonal-bingo? [board called]
  (or
   (every? #(.contains called %) (for [i (range 5)]
                                   (nth board (+ (* i 5) i))))
   (every? #(.contains called %) (for [i (range 1 6)]
                                   (nth board (- (* i 5) i))))))

(defn bingo? [board called]
  (or (column-bingo? board called)
      (row-bingo? board called)
      ; (diagonal-bingo? board called)
      ))
(defn score [board called]
  (let [uncalled (remove #(.contains called %) board)]
    (*
     (last called)
     (reduce + uncalled))))

(defn call-seqs [calls]
  (for [n (range 1 (inc (count calls)))]
    (take n calls)))

(defn part1 [boards all-calls]
  (first (filter (complement nil?)
                 (for [called (call-seqs all-calls)
                       board boards]
                   (if (bingo? board called)
                     (score board called)
                     nil)))))

(part1 test-boards test-draws) ; 4512

(def draws1 (get-draws input1))
(def boards1 (get-boards input1))

(part1 boards1 draws1) ; 2496

;

(defn part2 [all-boards all-calls]
  (loop [boards all-boards calls (call-seqs all-calls) last-bingo-score 0]
    (if (or (empty? boards) (empty? calls))
      last-bingo-score
      (let [not-yet (remove #(bingo? % (first calls)) boards)
            bingod (filter #(bingo? % (first calls)) boards)
            new-last (score (last bingod) (first calls))]
        (recur not-yet (next calls) new-last)))))

(part2 test-boards test-draws) ; 1924

(part2 boards1 draws1) ; 25925
