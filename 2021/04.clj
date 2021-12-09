(ns aoc
  (:require [clojure.string :as string]))

(defn split-input [input]
  (->
   input
   (string/replace #"[ ]{2,}" " ")
   (string/split #"\n\n")))

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

(defn bingo? [board called]
  (or (column-bingo? board called)
      (row-bingo? board called)))

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

(def draws1 (get-draws input1))
(def boards1 (get-boards input1))

(part1 boards1 draws1) ; 2496

(defn part2 [all-boards all-calls]
  (loop [boards all-boards calls (call-seqs all-calls) last-bingo-score 0]
    (if (or (empty? boards) (empty? calls))
      last-bingo-score
      (let [not-yet (remove #(bingo? % (first calls)) boards)
            bingod (filter #(bingo? % (first calls)) boards)
            new-last (score (last bingod) (first calls))]
        (recur not-yet (next calls) new-last)))))

(part2 boards1 draws1) ; 25925
