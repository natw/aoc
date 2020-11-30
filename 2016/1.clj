(ns aoc)

(def input (clojure.string/split  (clojure.string/trim (slurp "inputs/1.txt")) #", "))

; with state of x, y, direction, read though directions, moving

(defn right [[x y]] [y (* -1 x)])
(defn left [[x y]] [(* -1 y) x])

(defn parse-instruction [instruction]
  [(str (first instruction)) (Integer/parseInt (apply str (rest instruction)))])

(defn move [state instruction]
  (let [[turn-dir distance] (parse-instruction instruction)]
    (do
      (let [new-direction (apply (case turn-dir "R" right "L" left) [(:direction state)])
            move-vector (map (partial * distance) new-direction)
            new-loc (map + (:loc state) move-vector)]
        { :loc new-loc :direction new-direction }))))


(defn walk
  ([instructions] (walk instructions {:loc [0 0] :direction [0 1]}))
  ([instructions state]
   (if (empty? instructions)
     state
   (recur (rest instructions) (move state (first instructions))))))

(apply + (:loc (walk ["R2", "L3"])))
(println input)
(apply + (map #(Math/abs %) (:loc (walk input))))
