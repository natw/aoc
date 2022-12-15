(ns aoc.2022.14
  (:require [clojure.string :as str])
  (:require [clojure.set :as set])
  (:require [clojure.zip :as zip])
  (:require [clojure.math.combinatorics :as combo])
  (:require [clojure.walk :as walk]))

(def lines (str/split-lines (slurp "inputs/14.txt")))

(def test1 '("498,4 -> 498,6 -> 496,6"
             "503,4 -> 502,4 -> 502,9 -> 494,9"))

(defn parse-int [s]
  (Integer/parseInt s))

(defn mapmap [pred collcoll]
  (map (fn [i] (mapv pred i)) collcoll))

(defn rock-points [lines]
  (->> lines
       (mapcat (fn [line] (str/split line #" -> ")))
       (map #(str/split % #","))
       (mapmap parse-int)))

(rock-points test1)
