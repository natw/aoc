(ns aoc)

(def input-lines (clojure.string/split-lines (slurp "inputs/16-1.txt")))

(defn trim [s]
  (->
    s
    (clojure.string/replace "Before: " "")
    (clojure.string/replace "After: " "") 
    read-string
    eval))

(trim "Before: [2, 0, 2, 2]")


(Integer/parseInt "3")

(defn build-example [lines]
  (let [[before-line instructions-line after-line _] lines
        [opcode a b c] (map #(Integer/parseInt %) (clojure.string/split instructions-line #" "))
        ]
    {
     :before (trim before-line)
     :after (trim after-line)
     :opcode opcode
     :a a
     :b b
     :c c
     }
    )  
  )

(defn parse-examples [lines]
  (map build-example (partition 4 4 lines)))

(parse-examples input-lines)
(def examples (parse-examples input-lines))

(defn bint [b] (if b 1 0))

; rs == "registers", ie the before vector
(def ops
  [
   {:code 8 :name "addr" :fn (fn [rs a b c] (assoc rs c (+ (nth rs a) (nth rs b))))}
   {:code 10 :name "addi" :fn (fn [rs a b c] (assoc rs c (+ (nth rs a) b)))}
   {:code 2 :name "mulr" :fn (fn [rs a b c] (assoc rs c (* (nth rs a) (nth rs b))))}
   {:code 9 :name "muli" :fn (fn [rs a b c] (assoc rs c (* (nth rs a) b)))}
   {:code 4 :name "banr" :fn (fn [rs a b c] (assoc rs c (bit-and (nth rs a) (nth rs b))))}
   {:code 6 :name "bani" :fn (fn [rs a b c] (assoc rs c (bit-and (nth rs a) b)))}
   {:code 0 :name "borr" :fn (fn [rs a b c] (assoc rs c (bit-or (nth rs a) (nth rs b))))}
   {:code 5 :name "bori" :fn (fn [rs a b c] (assoc rs c (bit-or (nth rs a) b)))}
   {:code 14 :name "setr" :fn (fn [rs a b c] (assoc rs c (nth rs a)))}
   {:code 1 :name "seti" :fn (fn [rs a b c] (assoc rs c a))}
   {:code 12 :name "gtir" :fn (fn [rs a b c] (assoc rs c (bint (> a (nth rs b)))))}
   {:code 7 :name "gtri" :fn (fn [rs a b c] (assoc rs c (bint (> (nth rs a) b))))}
   {:code 15 :name "gtrr" :fn (fn [rs a b c] (assoc rs c (bint (> (nth rs a) (nth rs b)))))}
   {:code 13 :name "eqir" :fn (fn [rs a b c] (assoc rs c (bint (= a (nth rs b)))))}
   {:code 3 :name "eqri" :fn (fn [rs a b c] (do  
                                      ; (println "eqri-ing:" (nth rs a) b)
                                      (assoc rs c (bint (= (nth rs a) b)))))}
   {:code 11 :name "eqrr" :fn (fn [rs a b c] (assoc rs c (bint (= (nth rs a) (nth rs b)))))}
  ])


(defn works-for? [example op]
  (let [fun (op :fn)
        a (example :a)
        b (example :b)
        c (example :c)]
    (= (example :after) (fun (example :before) a b c))))

; part 1:
(count (filter (partial < 2) (map (fn [ex] (count (filter #(works-for? ex %) ops))) examples)))


; part 2:

(defn reducer [ops]
  (fn [acc ex]
    (let [opcode (ex :opcode)
          working-ops (set (map :name (filter #(works-for? ex %) ops)))
          codes-so-far (get acc opcode working-ops) ]
      (assoc acc opcode (clojure.set/intersection codes-so-far working-ops)))))

(def threexes (filter #(= 3 (% :opcode)) examples))

(defn figure-out-codes [ops' examples]
  (reduce (reducer ops') {} examples))

(figure-out-codes ops threexes)
; basically sudoku at this point. added codes to ops above

(def part-2-lines (clojure.string/split-lines (slurp "inputs/16-2.txt")))


(def instructions
  (map (fn [line] (map #(Integer/parseInt %) (clojure.string/split line #" "))) part-2-lines))

(first instructions)

(def ops-by-code
  (into (hash-map) (map #(vector (%1 :code) %1) ops)))

(defn operate [rs inst]
  (let [
        [opcode a b c] inst
        opfn ((get ops-by-code opcode) :fn)
        ]
    (opfn rs a b c)) )

(reduce operate [0 0 0 0] instructions)
