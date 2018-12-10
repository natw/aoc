(ns aoc)

(require '(clojure [zip :as zip]))
(require '(clojure.set))

(def input-lines (clojure.string/split-lines (slurp "inputs/7.txt")))

(first input-lines)


(def steps (map #(map second %) (map #(re-seq #"tep (\w) " %) input-lines)))

(first steps)

(def test-steps [[ \C \A ]
                 [ \C \F ]
                 [ \A \B ]
                 [ \A \D ]
                 [ \B \E ]
                 [ \D \E ]
                 [ \F \E ] ])


(defn parse-edges [steps]
  (loop [edges {} decs steps]
    (println decs)
    (if (empty? decs)
      edges
      (let [edge-spec (first decs) remaining (rest decs)]
        (recur (update-in edges [(first edge-spec)] #(cons (second edge-spec) %)) remaining))
      )

    )
    
  )


(parse-edges test-steps)

(defn find-roots [steps]
  (let [heads (map first steps) tails (map second steps)]
    (println heads)
    (clojure.set/difference (set heads) (set tails))
    ))

(find-roots test-steps) ; => C
(find-roots steps)

(defn insert-step [tree step]
  (let [found (atom false)]
    (def insert (fn insert [t' s]
      (when-let [t (seq tree)]
        (if (= (first t) (first step))
          ((reset! found true) 
           (concat t (rest step)))
          (map #(insert % step) (rest t))))))
    (insert tree step)
    (if (@found)
      tree
      ()
      )

    ))

(insert-step '() '(\A \B)) 

; take seq of steps and build tree structure 
; parents must be finished before children can begin
(defn build-tree [steps] 
  (loop [tree '() remaining' steps]
    (when-let [remaining (seq remaining')]
      (recur (insert-step tree (first remaining)) (next remaining)))))



(def expected-test-tree '(\C '(\A '(\B '(\E)) '(\D '(\E))) '(\F '(\E))))

(build-tree test-steps)


; walk tree and return steps in order
(defn order-tree [tree])



(order-tree expected-test-tree) ; CABDFE
