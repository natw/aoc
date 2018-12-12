(ns aoc)


(def initial-state "##....#.#.#...#.#..#.#####.#.#.##.#.#.#######...#.##....#..##....#.#..##.####.#..........#..#...#")

(def input-lines (clojure.string/split-lines (slurp "inputs/12.txt")))
(def rules (map #(clojure.string/split % #" => ") input-lines))

(first rules)
