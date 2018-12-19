(ns aoc)

(def starting-area (clojure.string/split-lines (slurp "inputs/18.txt")))

(defn get-acres [area]
  (for [[i row] (map-indexed list area)
        [j contents] (map-indexed list row)]
    (vector i j contents)))

(defn surrounding-coords [i j]
  (list
    [(dec i) (dec j)] [(dec i) j] [(dec i) (inc j)]
    [i (dec j)]                         [i (inc j)]
    [(inc i) (dec j)] [(inc i) j] [(inc i) (inc j)]))

(defn aget' [coll i j]
  (nth (nth coll i []) j nil))

; [ [i j contents] (surrounding-contents) ]
(defn windows [area]
  (map (fn [acre]
         (let [[i j _] acre]
           (vector acre (remove nil? (map #(apply aget' area %) (surrounding-coords i j))))
         )) (get-acres area)) )

(defn contains [window thing]
  (= thing (nth (first window) 2)))

(defn count-trees [window]
  (count (filter #(= \| %) (second window))))
(defn count-open [window]
  (count (filter #(= \. %) (second window))))
(defn count-lumberyards [window]
  (count (filter #(= \# %) (second window))))

(def rules
  [
  ])

(defn evolve [window]
  (cond
    (and (contains window \.) (<= 3 (count-trees window))) \|
    (and (contains window \|) (<= 3 (count-lumberyards window))) \#
    (and (contains window \#) (or (zero? (count-trees window)) (zero? (count-lumberyards window)))) \.
    :else (nth (first window) 2) ) )

(defn tick [area]
  (->>
    area
    (windows)
    (map evolve)
    (partition (count (nth area 0)) (count (nth area 0))))
  )

(nth (iterate tick starting-area) 4)


(def test-area
  [".#.#...|#."
   ".....#|##|"
   ".|..|...#."
   "..|#.....#"
   "#.#|||#|#|"
   "...#.||..."
   ".|....|..."
   "||...#|.#|"
   "|.||||..|."
   "...#.|..|."]
  )

(defn recombobulate [area]
  (map clojure.string/join area)
  )

(recombobulate (tick test-area))

(defn after [area minutes]
  (nth (iterate tick area) minutes)
  )


(defn score [area]
  (let [bits (apply concat area)]
    (* (count (filter #(= \# %) bits))
       (count (filter #(= \| %) bits))
       )
    )
  )

; part 2:

(defn find-repeat [start most]
  (let [areas (take most (iterate tick start))
        iterations (atom 1)
        score1 (score (first areas))]
    (do
      (println "first score: " score1)
    (for [area (rest areas)]
      (let [scr (score area)]
        (swap! iterations inc)
        (if (= scr score1)
          (println "pisssss!!!!" scr @iterations)) )))) )

(find-repeat starting-area 4000)

(score (after test-area 9))

(score (after starting-area 10))


(score (after starting-area 1000000000))
