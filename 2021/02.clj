(ns aoc
  (:require [clojure.string :as string]))

(defn string-lines [fname]
  (string/split-lines (slurp (format "inputs/%s" fname))))

; line is tuple of (horizontal-delta, vertical-delta)
(defn parse-line [line]
  (let [ps (string/split line #" ")
        dir (first ps)
        amt (Integer. (second ps)) ]
    (case dir
      "forward" (list amt 0)
      "up" (list 0 (-' amt))
      "down" (list 0 amt)
      )
    )
  )

(def input1 (map parse-line (string-lines "2")))

(defn part1 [starting-steps]
  (loop [pos '(0 0)
         steps starting-steps]
    (if (empty? steps)
      (reduce * pos)
      (recur
        (map + pos (first steps))
        (rest steps))))
)


(def test1 (map parse-line '("forward 5"
             "down 5"
             "forward 8"
             "up 3"
             "down 8"
             "forward 2")))

(part1 test1)
(part1 input1)
