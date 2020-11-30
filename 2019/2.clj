(ns aoc)

(defn parse-line [line]
  (vec (map #(Integer/parseInt %) (clojure.string/split (clojure.string/trim line) #","))))

(def prog1 (parse-line (slurp "inputs/2.txt")))

(defn doAdd [program idx]
  (let [a (program (program (+ idx 1)))
        b (program (program (+ idx 2)))
        destIdx (program (+ idx 3))]
    (assoc program destIdx (+ a b))))

(defn doMult [program idx]
  (let [a (program (program (+ idx 1)))
        b (program (program (+ idx 2)))
        destIdx (program (+ idx 3))]
    (assoc program destIdx (* a b))))

(defn tick [program idx]
  (let [opcode (program idx)]
    (case opcode
      99 program
      1 (tick (doAdd program idx) (+ idx 4))
      2 (tick (doMult program idx) (+ idx 4))
      (throw (Exception. "wut"))
      )
    ))

(defn run [program]
  (tick program 0)
  )

(def test1 (parse-line "1,0,0,0,99"))
(def test2 (parse-line "2,3,0,3,99"))
(def test3 (parse-line "2,4,4,5,99,0"))
(def test4 (parse-line "1,1,1,4,99,5,6,0,99"))

(run test1)
(run test2)

(def prog2 (assoc prog1 1 12 2 2))
((run prog2) 0)
