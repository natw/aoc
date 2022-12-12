(ns aoc
  (:require [clojure.string :as str])
  (:require [clojure.set :as set])
  (:require [clojure.zip :as zip])
  (:require [clojure.walk :as walk]))

(def lines (str/split-lines (slurp "inputs/11.txt")))
(def line-groups (partition 6 7 lines))

(def test1 '("Monkey 0:"
             "  Starting items: 79, 98"
             "  Operation: new = old * 19"
             "  Test: divisible by 23"
             "    If true: throw to monkey 2"
             "    If false: throw to monkey 3"
             ""
             "Monkey 1:"
             "  Starting items: 54, 65, 75, 74"
             "  Operation: new = old + 6"
             "  Test: divisible by 19"
             "    If true: throw to monkey 2"
             "    If false: throw to monkey 0"
             ""
             "Monkey 2:"
             "  Starting items: 79, 60, 97"
             "  Operation: new = old * old"
             "  Test: divisible by 13"
             "    If true: throw to monkey 1"
             "    If false: throw to monkey 3"
             ""
             "Monkey 3:"
             "  Starting items: 74"
             "  Operation: new = old + 3"
             "  Test: divisible by 17"
             "    If true: throw to monkey 0"
             "    If false: throw to monkey 1"))
(def test-groups (partition 6 7 test1))
(defn parse-int [s]
  (Integer/parseInt s))

(defn find-nums [s]
  (mapv parse-int (re-seq #"\d+" s)))

(defn find-num [s]
  (first (find-nums s)))

(defn parse-op [line]
  (let [[_ operator operand] (re-matches #"^\s+Operation: new = old ([*+/-]) (.+)$" line)
        fs (format "(%s old %s)" operator operand)]
    (def ^:dynamic old)
    (if (= operand "old")
      (fn [old] (* old old))
      (fn [old]
        ; (println "in-func operand:" operand)
        (let [operator' (ns-resolve *ns* (symbol operator))
              operand' (eval (read-string operand))]
          ; (println "in-func old:" old)
          (operator' old operand'))))))

; (do
;   (let [foo 6
;         v1 "2"
;         v2 "foo"
;         f' (fn [s] (println "s:" s) (eval (read-string s)))]
;     (println (+ 1 (f' v1))) ; 3
;     (println (f' "4"))
;     (println (f' "5"))
;     (println (+ 1 (f' v2))))) ; 7

(defn parse-line-group [grp]
  (let [i (find-num (first grp))
        items (mapv bigint (find-nums (second grp)))
        op (parse-op (nth grp 2))
        test-div (find-num (nth grp 3))
        true-target (find-num (nth grp 4))
        false-target (find-num (nth grp 5))
        inspections 0]
    [i items op test-div true-target false-target inspections]))

(defn monkeys [lines]
  (mapv parse-line-group (partition 6 7 lines)))

; (monkeys test1)

(defn throw-to [monkeys thrower]
  (let [m (nth monkeys thrower)
        item (first (nth m 1))
        test-div (nth m 3)
        true-target (nth m 4)
        false-target (nth m 5)
        target (if (= 0 (mod item test-div)) true-target false-target)]
    ; (println "thrower:" thrower)
    ; (println "    throwing" item "to:" target)
    (-> monkeys
        (update-in [thrower 1] (comp vec rest))
        (update-in [target 1] #(conj % item)))))

; (vec (conj [1 2] 3))
; (vec (rest [1 2 3]))
; (cons 3 '(1 2))
; (cons 3 [1 2])
; (conj [1 2] 2)
; (monkeys test1)
; (throw-to (monkeys test1) 1)

(defn inspect-first-item [monkeys i]
  (let [this-monkey (nth monkeys i)
        op (nth this-monkey 2)]
    ; (println "items before inspection:" (nth (nth monkeys i) 1))
    (let [r (-> monkeys
                (update-in [i 1 0] (fn [item]
                                     ; (println "updating worry" item "->" (unchecked-divide-int (op item) 3))
                                     (unchecked-divide-int (op item) 3)))
                (update-in [i 6] inc))]
      ; (println "items after inspection:" (nth (nth monkeys i) 1))
      r)))

; (monkeys test1)
; (throw-to (inspect-first-item (monkeys test1) 2) 2)

(defn do-turn [monkeys i]
  ; (let [[_ all-items _ _ true-target false-target inspections] (nth monkeys i)]
  (loop [monkeys monkeys]
    (let [items (nth (nth monkeys i) 1)]
      (if (empty? items)
        monkeys
        (recur (throw-to (inspect-first-item monkeys i) i))))))

; (monkeys test1)
; (do-turn (monkeys test1) 2)

((nth (nth (monkeys test1) 1) 2) 6)

(defn do-round [monkeys]
  (loop [monkeys monkeys n 0]
    ; (println "Monkey:" n)
    (if (= n (count monkeys))
      monkeys
      (recur (do-turn monkeys n) (inc n)))))

; (monkeys test1)
; (do-round (monkeys test1))

(defn part1 [lines]
  (let [monkeys (monkeys lines)]
    (as-> monkeys m
      (iterate do-round m)
      (nth m 20)
      (map #(nth % 6) m)
      (sort m)
      (take-last 2 m)
      (apply * m))))

; (part1 test1)

; (part1 lines)


; part 2


(defn inspect-first-item' [monkeys i]
  (let [this-monkey (nth monkeys i)
        op (nth this-monkey 2)]
    (-> monkeys
        (update-in [i 1 0] (fn [item] (op item)))
        (update-in [i 6] inc))))

(defn do-turn' [monkeys i]
  (loop [monkeys monkeys]
    (let [items (nth (nth monkeys i) 1)]
      (if (empty? items)
        monkeys
        (recur (throw-to (inspect-first-item' monkeys i) i))))))

(defn do-round' [monkeys]
  (loop [monkeys monkeys n 0]
    ; (println "Monkey:" n)
    (if (= n (count monkeys))
      monkeys
      (recur (do-turn' monkeys n) (inc n)))))

(defn part2 [lines]
  (let [monkeys (monkeys lines)]
    (as-> monkeys m
      (iterate do-round' m)
      (nth m 20))))
      ; (map #(nth % 6) m)
      ; (sort m)
      ; (take-last 2 m)
      ; (apply * m))))

(part2 test1)
