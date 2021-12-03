(ns aoc
  (:require [clojure.string :as string]))

(defn string-lines [fname]
  (string/split-lines (slurp (format "inputs/%s" fname))))

(def input1 (string-lines "3"))

(def test1 (map #(string/split % #"") (list
                                       "00100"
                                       "11110"
                                       "10110"
                                       "10111"
                                       "10101"
                                       "01111"
                                       "00111"
                                       "11100"
                                       "10000"
                                       "11001"
                                       "00010"
                                       "01010")))

(defn transpose [m]
  (apply map list m))

(defn most-least-common [digits]
  (let [f (frequencies digits)]
    (map key (list
              (apply max-key val f)
              (apply min-key val f)))))

(defn rates [readings]
  (->>
   (transpose readings)
   (map most-least-common)
   (transpose)
   (map #(string/join "" %))
   (map #(Integer/parseInt % 2))))

(defn part1 [readings]
  (reduce * (rates readings)))

(part1 test1) ; 198
(part1 input1) ; 3309596

; part 2

(defn most-common [digits]
  (key (apply max-key val (into (sorted-map) (frequencies digits)))))
(most-common '(b a b c a b a)) ; b
(most-common '("1" "0" "1" "0")) ; "1"
(most-common '("0" "1" "0" "1")) ; "1"
(defn least-common [digits]
  (key (apply min-key val (reverse (into (sorted-map) (frequencies digits))))))
(least-common '("1" "0" "1" "0")) ; "0"
(least-common '("0" "1" "0" "1")) ; "0"
(least-common '("1" "0" "1")) ; "0"

(defn match-bit-criteria [pred pos readings]
  (let [crit (pred (nth (transpose readings) pos))]
    (filter
     #(= (nth % pos) crit)
     readings)))

(defn bits2dec [bits]
  (Integer/parseInt (string/join "" bits) 2))

(defn rating [pred readings]
  (bits2dec (loop [x 0 readings' readings]
              (let [common-bit (pred (nth (transpose readings') x))
                    matching (filter #(= common-bit (nth % x)) readings')]
                (if (= 1 (count matching))
                  (first matching)
                  (recur (inc x) matching))))))

(defn oxy-rating [readings]
  (rating most-common readings))

(defn co2-rating [readings]
  (rating least-common readings))

(oxy-rating test1) ; 23
(co2-rating test1) ; 10

(* (oxy-rating input1) (co2-rating input1)) ; 2981085
