(ns aoc)

(def input-lines (clojure.string/split-lines (slurp "inputs/6.txt")))

; vector of vector pairs
(def input (vec (map (fn [pair] (vec (map #(Integer. %) pair))) (map #(clojure.string/split % #", ") input-lines))))

(def xbounds
  (let [xs (map first input)]
    (vector (apply min xs) (apply max xs))
  ))
(def ybounds
  (let [ys (map second input)]
    (vector (apply min ys) (apply max ys))
  ))

(def search-space)

(defn abs [n] (max n (-' n)))

(defn distance [a b]
  (let [[xa ya] a [xb yb] b]
    (+ (abs (- xa xb))
       (abs (- ya yb)))))

(defn memoize' [f]
  (let [mem (atom {})]
    (fn [& args]
      (if-let [e (find @mem args)]
        (val e)
        (let [ret (apply f args)]
          (swap! mem assoc args ret)
          ret)))))

(defn distance-from [coords]
  (partial distance coords))

(defn closest-location [locations point]
  (apply (partial min-key (distance-from point)) locations))
(def closest-location (memoize closest-location))

(= [2 2] (closest-location '([2 2] [10 10]) [1 1]))


; spiral outwards from point for some "infinite" area
(defn search-zone [point]
  (loop [n 0]
    
    )

  
  )


(defn explore-area [locations point]
  (let [area-size (atom 0)]
    (loop [remaining-locations (search-zone point)]
      (if (empty? remaining-locations)
        area-size
        ((if (= point (closest-location locations (first remaining-locations)))
           (swap! area-size inc))
           (recur (rest remaining-locations)))
        )
      )
    )
  )
