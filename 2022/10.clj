(ns aoc
  (:require [clojure.string :as str])
  (:require [clojure.set :as set])
  (:require [clojure.zip :as zip])
  (:require [clojure.walk :as walk]))

(def lines (str/split-lines (slurp "inputs/10.txt")))

(def test1 '("noop"
             "addx 3"
             "addx -5"))

(def test2 '("addx 15" "addx -11" "addx 6" "addx -3" "addx 5" "addx -1" "addx -8" "addx 13" "addx 4"
                       "noop" "addx -1" "addx 5" "addx -1" "addx 5" "addx -1" "addx 5" "addx -1" "addx 5"
                       "addx -1" "addx -35" "addx 1" "addx 24" "addx -19" "addx 1" "addx 16" "addx -11"
                       "noop" "noop" "addx 21" "addx -15" "noop" "noop" "addx -3" "addx 9" "addx 1" "addx -3"
                       "addx 8" "addx 1" "addx 5" "noop" "noop" "noop" "noop" "noop" "addx -36" "noop"
                       "addx 1" "addx 7" "noop" "noop" "noop" "addx 2" "addx 6" "noop" "noop" "noop" "noop"
                       "noop" "addx 1" "noop" "noop" "addx 7" "addx 1" "noop" "addx -13" "addx 13" "addx 7"
                       "noop" "addx 1" "addx -33" "noop" "noop" "noop" "addx 2" "noop" "noop" "noop" "addx 8"
                       "noop" "addx -1" "addx 2" "addx 1" "noop" "addx 17" "addx -9" "addx 1" "addx 1"
                       "addx -3" "addx 11" "noop" "noop" "addx 1" "noop" "addx 1" "noop" "noop" "addx -13"
                       "addx -19" "addx 1" "addx 3" "addx 26" "addx -30" "addx 12" "addx -1" "addx 3" "addx 1"
                       "noop" "noop" "noop" "addx -9" "addx 18" "addx 1" "addx 2" "noop" "noop" "addx 9"
                       "noop" "noop" "noop" "addx -1" "addx 2" "addx -37" "addx 1" "addx 3" "noop" "addx 15"
                       "addx -21" "addx 22" "addx -6" "addx 1" "noop" "addx 2" "addx 1" "noop" "addx -10"
                       "noop" "noop" "addx 20" "addx 1" "addx 2" "addx 2" "addx -6" "addx -11" "noop" "noop"
                       "noop"))

(defn parse-int [s]
  (Integer/parseInt s))

(defn expand-ops [ops]
  (mapcat (fn [op] (cond
                     (= op "noop") (list "noop")
                     :else (list "noop" op))) ops))

(defn tick [regs op]
  (cond
    (= op "noop") regs
    :else (let [amt (parse-int (re-find #"-?\d+" op))]
            (update-in regs [:x] (partial + amt)))))

(tick {:x 2} "addx 4")

(defn run
  ([ops] (run 1 {:x 1} (expand-ops ops)))
  ([cyc regs ops] (if (empty? ops)
                    (list)
                    (lazy-seq (cons [cyc regs]
                                    (run (inc cyc)
                                         (tick regs (first ops))
                                         (rest ops)))))))

(run test1)

(defn signal-strength [instructions cycle]
  (let [[cyc registers] (nth (run instructions) (dec cycle))]
    (* cyc (get registers :x))))

(nth (run test2) (dec 60))

(signal-strength test2 100)

(reduce + (map (partial signal-strength lines) '(20 60 100 140 180 220)))

;part 2

(defn run'
  ([ops] (run' 0 {:x 1} (expand-ops ops)))
  ([cyc regs ops] (if (empty? ops)
                    (list)
                    (lazy-seq (cons [cyc regs]
                                    (let [newregs (tick regs (first ops))]
                                      (run' (inc cyc)
                                            (tick regs (first ops))
                                            (rest ops))))))))

(defn render [cyc regs]
  (let [sprite-pos (get regs :x)
        pixel (if (<= (abs (- sprite-pos (mod cyc 40)))
                      1)
                "#"
                ".")]
    ; (println "rendering - crt:" cyc "sprite:" sprite-pos "pixel:" pixel)
    pixel))

(defn screen [ops]
  (map (partial apply render) (run' ops)))

(defn part2 [lines]
  (map (fn [grp] (str/join "" grp)) (partition 40 (screen lines))))

(part2 test2)
(part2 (take 80 test2))

(part2 lines)
