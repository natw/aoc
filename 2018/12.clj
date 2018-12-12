(ns aoc)


(def initial-state "##....#.#.#...#.#..#.#####.#.#.##.#.#.#######...#.##....#..##....#.#..##.####.#..........#..#...#")

(def input-lines (clojure.string/split-lines (slurp "inputs/12.txt")))
(defn make-rules [lines]
  (apply hash-map (flatten (map #(clojure.string/split % #" => ") lines)))
  )
(def rules (make-rules input-lines))
(first rules)

(defn tick [rules index state]
  (let [state' (str "...." state ".....") index' (- index 2)]
    [index' (clojure.string/join (map (fn [seg] (rules (clojure.string/join seg) ".")) (partition 5 1 state')))]))


(def test-state "#..#.#..##......###...###")
(def test-rule-lines [ "...## => #"
                      "..#.. => #"
                      ".#... => #"
                      ".#.#. => #"
                      ".#.## => #"
                      ".##.. => #"
                      ".#### => #"
                      "#.#.# => #"
                      "#.### => #"
                      "##.#. => #"
                      "##.## => #"
                      "###.. => #"
                      "###.# => #"
                      "####. => #"
                      ])
(def test-rules (make-rules test-rule-lines))

(map vector '(1 2 3) (range 12))

(def test-gens
  (vec (map vector (repeat -3)
  [
   "...#..#.#..##......###...###..........."
   "...#...#....#.....#..#..#..#..........."
   "...##..##...##....#..#..#..##.........."
   "..#.#...#..#.#....#..#..#...#.........."
   "...#.#..#...#.#...#..#..##..##........."
   "....#...##...#.#..#..#...#...#........."
   "....##.#.#....#...#..##..##..##........"
   "...#..###.#...##..#...#...#...#........"
   "...#....##.#.#.#..##..##..##..##......."
   "...##..#..#####....#...#...#...#......."
   "..#.#..#...#.##....##..##..##..##......"
   "...#...##...#.#...#.#...#...#...#......"
   "...##.#.#....#.#...#.#..##..##..##....."
   "..#..###.#....#.#...#....#...#...#....."
   "..#....##.#....#.#..##...##..##..##...."
   "..##..#..#.#....#....#..#.#...#...#...."
   ".#.#..#...#.#...##...#...#.#..##..##..."
   "..#...##...#.#.#.#...##...#....#...#..."
   "..##.#.#....#####.#.#.#...##...##..##.."
   ".#..###.#..#.#.#######.#.#.#..#.#...#.."
   ".#....##....#####...#######....#.#..##."
   ]))
  )

(tick test-rules 0 test-state)

(defn trim [state index]
  [state index]

  )

(defn run [initial-state rules gens']
  (loop [state initial-state gens gens' index 0]
    (if (= 0 (mod gens 100))
      (let [trimmed-pair (trim state)]
        (let [[index' state'] trimmed-pair]
          (let [[new-index new-state] (tick rules index' state')]
            (recur new-state (dec gens) new-index))))
      )
      (if (= gens 1)
        (tick rules index state)
        (let [[new-index new-state] (tick rules index state)]
          (recur new-state (dec gens) new-index))) ))


(defn shift-left [pair offset]
  (let [[index state] pair]
  [
   (+ index offset)
   (clojure.string/join (nthrest state offset))
   ]) )

(let [x 2]
(println "\n"
         (shift-left (run test-state test-rules x) 2)
         "\n"
         (shift-left (get test-gens x) 1)))

(run test-state test-rules 20)

(defn score [pair]
  (let [[offset state] pair]
    (reduce +
            (map first
                 (filter
                   (fn [p] (= (second p) \#))
                   (map vector
                        (range offset (java.lang.Integer/MAX_VALUE))
                        state))))))

(score (run test-state test-rules 20))
(score (run initial-state rules 50000000000))
