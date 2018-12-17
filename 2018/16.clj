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

(def examples (parse-examples input-lines))
