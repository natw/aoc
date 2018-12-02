(ns aoc)

(def input (clojure.string/split-lines (slurp "inputs/input2.txt")))

(defn contains-double? [code] (some #{2} (vals (frequencies code))))
(defn contains-triple? [code] (some #{3} (vals (frequencies code))))

(defn checksum [codes]
  (* (count (filter contains-double? codes))
     (count (filter contains-triple? codes))))

(checksum input)

; part 2

(defn all-pairs [coll]
  (loop [[x & xs] coll
         result []]
    (if (nil? xs)
      result
      (recur xs (apply conj result (map #(vector x %) xs))))))


(map #(apply map vector %) (all-pairs input))

; number of chars in strings a and be that differ
(defn differences [a b]
  (count (filter #(apply not= %) (map vector a b))))

(assert (= 3 (differences "abc" "def")))
(assert (= 1 (differences "abc" "abq")))


(first (filter #(= 1 (apply differences %)) (all-pairs input)))
; this probably wasn't a great way to approach the problem.
; I didn't read it carefully enough to see that what they were really asking for
; was the letters in common
