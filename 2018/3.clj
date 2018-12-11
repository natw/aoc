(ns aoc)


(def input (clojure.string/split-lines (slurp "inputs/input3.txt")))

(def inputs
  (map (fn [line]
         (let [ms (re-matches #"\#\d+ @ (\d+),(\d+): (\d+)x(\d+)" line)]
           {:xpos (Integer. (get ms 1))
            :ypos (Integer. (get ms 2))
            :width (Integer. (get ms 3))
            :height (Integer. (get ms 4))}))
       input))

(first inputs)

(def fabric (to-array-2d (repeat 1200 (repeat 1200 0))))

(defn add-claim [fab claim]
  (for [x (range (:xpos claim) (:width claim))
        y (range (:ypos claim) (:height claim))]
    (update-in fab [x y] inc)))

(defn populate-claims [fab initial-claims]
  (loop [claims initial-claims]
    (if (> 0 (count claims)) 
      (do 
        (add-claim fab (first claims))
        (recur (rest claims)))
      fab)))

(def f2 (populate-claims fabric inputs))

(class (first f2))
(aget fabric 109 351)
