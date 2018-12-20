(ns aoc)

(def input-lines (clojure.string/split-lines (slurp "inputs/19.txt")))

(defn bint [b] (if b 1 0))
(def ops
  [
   {:code 0 :name "borr" :fn (fn [rs a b c] (assoc rs c (bit-or (nth rs a) (nth rs b))))}
   {:code 1 :name "seti" :fn (fn [rs a b c] (assoc rs c a))}
   {:code 10 :name "addi" :fn (fn [rs a b c] (assoc rs c (+ (nth rs a) b)))}
   {:code 11 :name "eqrr" :fn (fn [rs a b c] (assoc rs c (bint (= (nth rs a) (nth rs b)))))}
   {:code 12 :name "gtir" :fn (fn [rs a b c] (assoc rs c (bint (> a (nth rs b)))))}
   {:code 13 :name "eqir" :fn (fn [rs a b c] (assoc rs c (bint (= a (nth rs b)))))}
   {:code 14 :name "setr" :fn (fn [rs a b c] (assoc rs c (nth rs a)))}
   {:code 15 :name "gtrr" :fn (fn [rs a b c] (assoc rs c (bint (> (nth rs a) (nth rs b)))))}
   {:code 2 :name "mulr" :fn (fn [rs a b c] (assoc rs c (* (nth rs a) (nth rs b))))}
   {:code 3 :name "eqri" :fn (fn [rs a b c]  (assoc rs c (bint (= (nth rs a) b))))}
   {:code 4 :name "banr" :fn (fn [rs a b c] (assoc rs c (bit-and (nth rs a) (nth rs b))))}
   {:code 5 :name "bori" :fn (fn [rs a b c] (assoc rs c (bit-or (nth rs a) b)))}
   {:code 6 :name "bani" :fn (fn [rs a b c] (assoc rs c (bit-and (nth rs a) b)))}
   {:code 7 :name "gtri" :fn (fn [rs a b c] (assoc rs c (bint (> (nth rs a) b))))}
   {:code 8 :name "addr" :fn (fn [rs a b c] (assoc rs c (+ (nth rs a) (nth rs b))))}
   {:code 9 :name "muli" :fn (fn [rs a b c] (assoc rs c (* (nth rs a) b)))}
  ])
(def ops-by-name
  (into (hash-map) (map #(vector (%1 :name) %1) ops)))


(defn parse-line [line]
  (let [[code & xs] (clojure.string/split line #" ")]
    (vec (cons code (map #(Integer/parseInt %) xs)))))

(def input (map parse-line (rest input-lines)))


; ipb = instruction pointer binding
(defn work [ipb rs' instructions]
  (defn ip [rs] (nth rs ipb))
  (loop [rs rs']
    ; (println "instruction pointer: " (ip rs))
    (if (>= (ip rs) (count instructions))
      (do
      rs)
      (let [inst (nth instructions (ip rs))
            [code a b c] inst
            opfn (:fn (get ops-by-name code))
            new-rs (opfn rs a b c)
            
            ]
        (do
          ; (println "registers:" rs)
          ; (println "op:" code)
          (println "new rs:" new-rs)
        (recur (update-in (opfn rs a b c) [ipb] inc))) ) ) ))

(work 2 [0 0 0 0 0 0] input)
(work 2 [0 1 0 0 0 0] input)

1
