(ns aoc
  (:require [clojure.string :as str])
  (:require [clojure.set :as set])
  (:require [clojure.zip :as zip])
  (:require [clojure.walk :as walk]))

(def lines (str/split-lines (slurp "inputs/08.txt")))

(def test1 '("30373"
             "25512"
             "65332"
             "33549"
             "35390"))

(defn parse-int [s]
  (Integer/parseInt s))

(defn parse-lines [lines]
  (map
   (fn [l] (map parse-int (str/split l #"")))
   lines))

(parse-lines '("12" "23"))

(defn visible-from-left? [forest x y]
  (let [row (nth forest x)
        val (nth row y)]
    (every? #(< % val) (take y row))))

(defn visible-from-right? [forest x y]
  (let [row (nth forest x)
        val (nth row y)]
    (every? #(< % val) (nthrest row (inc y)))))

(visible-from-left? (list '(2 1 3 1 2)) 0 2) ; true
(visible-from-left? (list '(2 1 3 1 2)) 0 1) ; false
(visible-from-right? (list '(2 1 3 1 2)) 0 2) ; true
(visible-from-right? (list '(2 1 3 1 2)) 0 1)  ; false

(nth (to-array-2d '((1 2) '(3 4))) 0)

(defn visible-from-top? [forest x y]
  (let [col (map #(nth % y) forest)
        val (nth col x)]
    (every? #(< % val) (take x col))))

(visible-from-top? (list '(2)
                         '(1)
                         '(3)) 2 0) ; true
(visible-from-top? (list '(2)
                         '(1)
                         '(3)) 1 0) ; false

(defn visible-from-bottom? [forest x y]
  (let [col (map #(nth % y) forest)
        val (nth col x)]
    (every? #(< % val) (nthrest col (inc x)))))

(visible-from-bottom? (list '(2) '(1) '(3) '(1) '(2)) 2 0) ; true
(visible-from-bottom? (list '(2) '(1) '(3) '(1) '(2)) 1 0) ; false

; x is top down (top row is 0)
; y is left-right
(defn is-visible? [forest x y]
  (let [xlen (count forest)
        ylen (count (first forest))]
    (cond
      (= x 0) true
      (= x xlen) true
      (= y 0) true
      (= y ylen) true
      :else (or (visible-from-left? forest x y)
                (visible-from-right? forest x y)
                (visible-from-top? forest x y)
                (visible-from-bottom? forest x y)))))

(defn coords [forest]
  (for [x (range (count (first forest)))
        y (range (count forest))]
    (list forest x y)))

(def f (parse-lines lines))
(def t (parse-lines test1))

(is-visible? t 1 3)

(defn count-visible [lines]
  (count (filter true? (map #(apply is-visible? %) (coords (parse-lines lines))))))

(count-visible test1)
; part 1
(count-visible lines)

; part2

(defn view-forward [forest x y forward-items]
  (let [val (nth (nth forest x) y)
        smaller (take-while #(< % val) forward-items)
        next-one (get (vec forward-items) (inc (count smaller)))]
    (cond
      (empty? forward-items) 0
      (= 1 (- (count forward-items) (count smaller))) (count forward-items)
      (nil? next-one) (count smaller)
      :else (inc (count smaller)))))

(defn view-left [forest x y]
  (let [forward-items (reverse (take y (nth forest x)))]
    (view-forward forest x y forward-items)))

(defn view-right [forest x y]
  (let [forward-items (nthrest (nth forest x) (inc y))]
    (view-forward forest x y forward-items)))

(defn view-up [forest x y]
  (let [col (map #(nth % y) forest)
        forward-items (reverse (take x col))]
    (view-forward forest x y forward-items)))

(defn view-down [forest x y]
  (let [col (map #(nth % y) forest)
        forward-items (nthrest col (inc x))]
    (view-forward forest x y forward-items)))

(defn scenic-score [forest x y]
  (* (view-left forest x y)
     (view-right forest x y)
     (view-up forest x y)
     (view-down forest x y)))

(view-up t 3 2) ; 2
(view-left t 3 2) ; 2
(view-down t 3 2) ; 1
(view-right t 3 2)

(defn part2 [lines]
  (let [tree (parse-lines lines)]
    (apply max (map #(apply scenic-score %) (coords tree)))))

(scenic-score t 4 3)
(view-up t 4 3)
(view-left t 4 3)
(view-down t 4 3)
(view-right t 4 3)

(part2 test1)

(part2 lines)
