(ns aoc.2022.16
  (:require [clojure.string :as str])
  (:require [clojure.set :as set])
  (:require [clojure.zip :as zip])
  (:require [clojure.math.combinatorics :as combo])
  (:require [clojure.walk :as walk]))

(def lines (str/split-lines (slurp "inputs/16.txt")))

(def test1 '("Valve AA has flow rate=0; tunnels lead to valves DD, II, BB"
             "Valve BB has flow rate=13; tunnels lead to valves CC, AA"
             "Valve CC has flow rate=2; tunnels lead to valves DD, BB"
             "Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE"
             "Valve EE has flow rate=3; tunnels lead to valves FF, DD"
             "Valve FF has flow rate=0; tunnels lead to valves EE, GG"
             "Valve GG has flow rate=0; tunnels lead to valves FF, HH"
             "Valve HH has flow rate=22; tunnel leads to valve GG"
             "Valve II has flow rate=0; tunnels lead to valves AA, JJ"
             "Valve JJ has flow rate=21; tunnel leads to valve II"))

(defn parse-int [s]
  (Integer/parseInt s))
(defn parse-line [line]
  (let [[_ name flow-rate leads-to'] (re-matches #"^Valve (\w+) has flow rate=(\d+); tunnels? leads? to valves? (.+)$" line)
        leads-to (str/split leads-to' #", ")]
    [name (parse-int flow-rate) leads-to]))

(defn parse-lines [lines]
  (map parse-line lines))
