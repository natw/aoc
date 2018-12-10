(ns aoc)

; input :
; 455 players
; last marble score of 71223

(nthrest '(1 2 3 4 5) 2)

(defn insert-marble [circle to-place]
  (concat (list to-place) (nthrest circle 2) (take 2 circle)) )

(insert-marble '(1 2 3 4 5 6) 3)

(defn remove-marble [circle]
  (let [removed-position (mod -7 (count circle))]
    [ (concat (nthrest circle (+ removed-position 1))
            (take removed-position circle))
      (nth circle removed-position)
     ]))

(remove-marble (range 1 11))

(defn find-high-score [num-players last-marble-score]
  (let [scores (atom {})]
    (loop [circle [0]
           remaining (range 1 (+ last-marble-score 1))
           current-player 0]
      (if remaining
        (let [[to-place & remaining'] remaining]
          (if (= 0 (mod to-place 23))
            (do
              (swap! scores #(update-in % [current-player] (fn [s] (+ (or s 0) to-place))))
              (let [[circle removed-marble] (remove-marble circle)]
                (swap! scores #(update-in % [current-player] (fn [s] (+ s removed-marble))))
                (recur circle remaining' (mod (inc current-player) num-players))) )
            (do
              ; (println (insert-marble circle to-place))
              (recur (insert-marble circle to-place) remaining' (mod (inc current-player) num-players)))))
        (apply max (vals @scores))))))

(find-high-score 9 25) ; => 32
(find-high-score 10 1618) ; => 8317
(find-high-score 13 7999) ; => 146373
(find-high-score 17 1104) ; => 2764
(find-high-score 21 6111) ; => 54718
(find-high-score 30 5807) ; => 37305

(find-high-score 455 71223)
