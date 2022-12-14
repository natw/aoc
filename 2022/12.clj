(ns aoc
  (:require [clojure.string :as str])
  (:require [clojure.set :as set])
  (:require [clojure.zip :as zip])
  (:require [clojure.math.combinatorics :as combo])
  (:require [clojure.walk :as walk]))

(def lines (str/split-lines (slurp "inputs/12.txt")))
(def start [20 0])

(def test1 '("Sabqponm"
             "abcryxxl"
             "accszExk"
             "acctuvwj"
             "abdefghi"))

(def test-start [0 0])

(def test2 '("SbcdefghijklmnopqrstuvwxyE"
             "aazzzzzzzzzzzzzzzzzzzzaaaa"))

(defn make-topo [lines]
  (to-array-2d (map (fn [row] (map int row)) lines)))

(def topo (make-topo test1))

(defn get-height [topo pos]
  (let [h (apply (partial aget topo) pos)]
    (cond
      (= h (int \S)) (int \a)
      (= h (int \E)) (int \z)
      :else h)))

(defn all-neighbors [topo pos]
  (let [[r c] pos
        points (list [(inc r) c]
                     [(dec r) c]
                     [r (inc c)]
                     [r (dec c)])]
    (filter (fn [[r c]] (and (not (neg? r))
                             (not (neg? c))
                             (not (= [r c] pos))
                             (< r (count topo))
                             (< c (count (first topo))))) points)))

(defn steppable? [topo pos1 pos2]
  (let [h1 (get-height topo pos1)
        h2 (get-height topo pos2)]
    (<= (- h2 h1) 1)))

(all-neighbors topo test-start)

(defn is-end? [topo pos]
  (= (int \E) (apply (partial aget topo) pos)))

(defn steppable-neighbors [topo pos]
  (->> (all-neighbors topo pos)
       (filter #(steppable? topo pos %))
       (vec)))

(defn get-distance [topo pos]
  (loop [seen #{pos}
         queue [[0 pos]]]
    (let [[dist current] (first queue)]
      (if (or (nil? current) (is-end? topo current))
        dist
        (let [nbrs (remove seen (steppable-neighbors topo current))
              costed-nbrs (map #(vector (inc dist) %) nbrs)]
            (recur (reduce conj seen nbrs)
                  (reduce conj (vec (rest queue)) costed-nbrs)))))))

(defn part1 [lines start]
  (let [topo (make-topo lines)]
    (get-distance topo start)))

; (part1 test1 test-start)
(part1 lines start)

; part 2

(defn a-positions [topo]
  (for [row (range (count topo))
        col (range (count (first topo)))
        :let [pos (vector row col)]
        :when (= (int \a) (get-height topo pos))]
    pos))

(defn part2 [lines]
  (let [topo (make-topo lines)
        as (a-positions topo)]
    (->> as
         (map #(get-distance topo %))
         (remove nil?)
         (apply min))))

(part2 lines)
