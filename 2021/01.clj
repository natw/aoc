(ns aoc
 (:require [clojure.string :as str]))

(def test1 (vector 199 200 208 210 200 207 240 269 260 263))

(def input (map str (seq (first (str/split-lines (slurp "inputs/1"))))))

(defn count-increases [depths]
  (count
   (filterv
    #(apply < %)
    (partition 2 1 depths))))

(defn sum [x] (reduce + x))

(defn count-increases2 [depths]
  (count
   (filterv
    #(apply < %)
    (map #(reduce + %) (mapcat
                        #(partition 3 1 %)
                        (partition 4 1 depths))))))

(count-increases test1)
(count-increases2 test1)

(count-increases input)
(count-increases2 input)

(map #(reduce + %)
     (partition 2 1
                (mapcat
                 #(partition 3 1 %)
                 (partition 4 1 test1))))

(partition 2 1
           (mapcat
            #(partition 3 1 %)
            (partition 4 1 test1)))

; part 1
(count
 (filterv
  #(apply < %)
  (partition 2 1 input)))

; part 2
(count (filterv
        #(apply < %)
        (map
         #(map sum %)
         (partition 2 1 (partition 3 1 input)))))
