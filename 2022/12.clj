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

(all-neighbors topo [0 0])

(defn steppable? [topo pos1 pos2]
  (let [h1 (get-height topo pos1)
        h2 (get-height topo pos2)]
    (<= (abs (- h1 h2)) 1)))

(all-neighbors topo test-start)

(defn is-end? [topo pos]
  (= (int \E) (apply (partial aget topo) pos)))


(let [iters (atom 0)
      cache (atom {})
      visited (atom #{})]

  (defn steppable-neighbors [topo pos]
    (->> (all-neighbors topo pos)
        (filter #(steppable? topo pos %))
        (filter #(not (contains? @visited %)))))

  (defn get-distance [topo pos]
    (swap! iters inc)
    (swap! visited conj pos)
    (println "getting distance" @iters)
    (if (is-end? topo pos)
      0
      (if-some [cached-dist (get @cache pos)]
        (do
          ; (println "had cached dist for" pos)
          cached-dist)
        (let [nbrs (steppable-neighbors topo pos)
              ; _ (println nbrs)
              f (fn [p] (get-distance topo p))
              nbr-dists (map f nbrs)
              min-step (if (empty? nbr-dists) Integer/MAX_VALUE (apply min nbr-dists))
              this-dist (inc min-step)]
          (swap! cache assoc pos this-dist)
          this-dist))))

  (defn part1 [lines start]
    (let [topo (make-topo lines)]
      (get-distance topo start)))

  ; (part1 test2 test-start))
  (part1 test1 test-start))

  ; (part1 lines start))
