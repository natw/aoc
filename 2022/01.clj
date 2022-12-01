(ns aoc
  (:require [clojure.string :as str]))

(def input (str/split-lines (slurp "inputs/01.txt")))

(first input)

(defn parse-int [x]
  (Integer/parseInt x))

(defn elf-total [cal-list]
  (reduce + (map parse-int cal-list)))


(elf-total '("1" "2"))

(def totals (->> (partition-by empty? input)
                (remove #(= '("") %))
                (map elf-total)))
(apply max totals) ; 68787

(reduce + (take-last 3 (apply sorted-set totals))) ; 198041
