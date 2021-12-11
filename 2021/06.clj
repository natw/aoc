(ns aoc
  (:require [clojure.string :as string]))

(defn split-input [i]
  (map #(Integer. %) (string/split i #",")))

(def input (split-input "2,3,1,3,4,4,1,5,2,3,1,1,4,5,5,3,5,5,4,1,2,1,1,1,1,1,1,4,1,1,1,4,1,3,1,4,1,1,4,1,3,4,5,1,1,5,3,4,3,4,1,5,1,3,1,1,1,3,5,3,2,3,1,5,2,2,1,1,4,1,1,2,2,2,2,3,2,1,2,5,4,1,1,1,5,5,3,1,3,2,2,2,5,1,5,2,4,1,1,3,3,5,2,3,1,2,1,5,1,4,3,5,2,1,5,3,4,4,5,3,1,2,4,3,4,1,3,1,1,2,5,4,3,5,3,2,1,4,1,4,4,2,3,1,1,2,1,1,3,3,3,1,1,2,2,1,1,1,5,1,5,1,4,5,1,5,2,4,3,1,1,3,2,2,1,4,3,1,1,1,3,3,3,4,5,2,3,3,1,3,1,4,1,1,1,2,5,1,4,1,2,4,5,4,1,5,1,5,5,1,5,5,2,5,5,1,4,5,1,1,3,2,5,5,5,4,3,2,5,4,1,1,2,4,4,1,1,1,3,2,1,1,2,1,2,2,3,4,5,4,1,4,5,1,1,5,5,1,4,1,4,4,1,5,3,1,4,3,5,3,1,3,1,4,2,4,5,1,4,1,2,4,1,2,5,1,1,5,1,1,3,1,1,2,3,4,2,4,3,1"))

(def test1 (split-input "3,4,3,1,2"))

(defn step [fish]
  (if (zero? fish)
    '(6 8)
    (list (dec fish))))

(defn tick [fish]
  (mapcat step fish))

(defn part1 [times fish']
  (count (loop [n times fish fish']
           (if (= n 0)
             fish
             (recur (dec n) (tick fish))))))

(part1 18 test1) ; 26
(part1 80 test1)
; (part1 80 input) ; 358214

(defn dec-keys [m]
  (reduce-kv (fn [m k v] (assoc m (dec k) v)) {} m))

(defn tick2 [fish-report]
  (let [mommies (get fish-report 0 0)]
    (-> fish-report
        (assoc 0 0)
        (dec-keys)
        (dissoc -1)
        (update 6 #(if (nil? %) mommies (+ % mommies)))
        (update 8 #(if (nil? %) mommies (+ % mommies))))))

(defn part2 [times fish]
  (loop [n times
         fr (frequencies fish)]
    (if (zero? n)
      (reduce + (vals fr))
      (recur (dec n) (tick2 fr)))))

; (defn part2 [times fish]
;   (let [x (part1 times '(0))]
;     (+ (reduce + fish) (* x (dec (count fish))))))

(part2 18 test1)
(part2 80 test1)
(part2 256 test1)
(part2 256 input) ; 1622533344325
