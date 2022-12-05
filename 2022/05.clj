(ns aoc
  (:require [clojure.string :as str])
  (:require [clojure.set :as set]))

(def lines1 (str/split-lines (slurp "inputs/05.txt")))

(defn split-sections [lines]
  (partition-by empty? lines))

(split-sections '("a" "b" "" "c"))

(defn parse-stack-line [stack-count line]
  (let [indexes (map #(+ 1 (* 4 %)) (range 0 stack-count))]
    (map #(get line % \space) indexes)))

(parse-stack-line 3 "    [M]")

(defn filter-spaces [chars]
  (filter #(not (= \space %)) chars))

(defn parse-stacks [lines]
  (let [stack-count (parse-int (last (re-seq #"\d+" (last lines))))
        line-parser (partial parse-stack-line stack-count)
        stack-lines (butlast lines)
        parsed-stack-lines (map line-parser stack-lines)]
    (map filter-spaces (apply map list parsed-stack-lines))))

(def test-lines '("    [D]    "
                  "[N] [C]    "
                  "[Z] [M] [P]"
                  " 1   2   3 "
                  ""
                  "move 1 from 2 to 1"
                  "move 3 from 1 to 3"
                  "move 2 from 2 to 1"
                  "move 1 from 1 to 2"))

(defn get-stack-lines [all-lines]
  (first (split-sections all-lines)))

(get-stack-lines test-lines)

(parse-stacks (get-stack-lines test-lines)) ; this gets stacks from input lines



(defn get-rule-lines [lines]
  (last (split-sections lines)))

(get-rule-lines test-lines)

(defn parse-int [x]
  (Integer/parseInt x))

(defn parse-rule-line [line]
  (map parse-int (re-seq #"\d+" line)))

(defn parse-rule-lines [lines]
  (map parse-rule-line lines))

(defn stack-handler [from to target]
  (fn [index stack]
    (condp = index
      (dec from) (rest stack)
      (dec to) (cons target stack)
      stack)))

(defn move [from to stacks]
  (let [movee (first (nth stacks (dec from)))]
    (map-indexed (stack-handler from to movee) stacks)))

(defn apply-rule [[times from to] stacks]
  (let [rule-applications (iterate (partial move from to) stacks)]
    (nth rule-applications times)))

(defn part1 [all-lines]
  (loop [stacks (parse-stacks (get-stack-lines all-lines))
         rules (parse-rule-lines (get-rule-lines all-lines))]
    (if (empty? rules)
      stacks
      (recur (apply-rule (first rules) stacks) (rest rules)))))

; (parse-stacks (get-stack-lines test-lines))
; (parse-rule-lines (get-rule-lines test-lines))
; (apply-rule (list 1 1 2) (parse-stacks (get-stack-lines test-lines)))

(str/join "" (map first (part1 lines1))) ; "QGTHFZBHV"

; part 2

(defn move2 [n from to stacks]
  (let [source-stack (nth stacks (dec from))
        tomove (take n source-stack)
        new-source (drop n source-stack)
        dest-stack (nth stacks (dec to))
        new-dest (concat tomove dest-stack)
        from' (dec from)
        to' (dec to)]
    (println source-stack)
    (map-indexed (fn [i, s]
                   (condp = i
                    from' new-source
                    to' new-dest
                    s)) stacks)))

(defn apply-rule' [[n from to] stacks]
  (move2 n from to stacks))

(def test-stacks (parse-stacks (get-stack-lines test-lines)))
(def test-rules (parse-rule-lines (get-rule-lines test-lines)))

(move2 1 2 3 test-stacks)
(apply-rule' (first test-rules) test-stacks)

test-stacks
(get test-stacks 1)

(defn part2 [all-lines]
  (loop [stacks (parse-stacks (get-stack-lines all-lines))
         rules (parse-rule-lines (get-rule-lines all-lines))]
    (if (empty? rules)
      stacks
      (recur (apply-rule' (first rules) stacks) (rest rules)))))

(part2 test-lines)

(str/join "" (map first (part2 lines1))) ; "MGDMPSZTM"
