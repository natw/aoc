(ns aoc)

(def input-lines (clojure.string/split-lines (slurp "inputs/15.txt")))

; parse map - find actors (elfs and goblins), register them, and replace with \.
; actors' actions don't modify map array
; when reading map, first check actor reference, then map array
; assuming actual map is immutable... it better be


(defn remove-actors [cave]
  (map #(replace {\G \. \E \.} %) cave))

(def cave (remove-actors input-lines))

; `input` is the map array (2d java array)
; returns actor registry
; NOTE!!! for ease of ordering, coordinates are [y, x], with the origin
; in the top left
(defn extract-actors [input]
  (apply merge (map #(sorted-map (first %1) (second %1)) 
                    (let [actor-ids (atom 0)]
                      (for [
                            [i row] (map-indexed list input)
                            [j cell] (map-indexed list row)
                            :when (some #{cell} [\G \E])
                            ]
                        (let [coords [i j]
                              id (str cell (swap! actor-ids inc))
                              actor {:id id :type cell :health 200 :power 3 :y i :x j} ]
                          [ id, actor ]) )))))

(def actor-registry (extract-actors input-lines))

(defn reading-order [actors]
  (sort-by #(vector (:y %1) (:x %1)) actors))

(defn keyed-by-coords [actors]
  (into (sorted-map) (map #(vector (vector (:y %1) (:x %1)) %1) actors)))

(defn is-enemy? [actor other]
  (not= (actor :type) (other :type)))

(defn find-adjacent-enemies [actor actors]
  (let [abc (keyed-by-coords actors) y (actor :y) x (actor :x)]
    (->> [(get abc [(dec y) x])
          (get abc [y (dec x)])
          (get abc [y (inc x)])
          (get abc [(inc y) x])]
         (remove nil?)
         (filter is-enemy?)
         (first))))

(defn surrounding [coords]
  (let [[y x] coords]
    [ [(dec y) x]
      [y (dec x)]
      [y (inc x)]
      [(inc y) x] ]))

(defn unoccupied? [cave actors coords]
  (and (= (get-in cave coords) \.)
       (not (contains? actors coords))))

(defn adjacent-spaces [cave actors coords]
  (-> (surrounding coords)
      (filter #(unoccupied? cave actors %))))

; do a thing
; return updated registry
(defn take-turn [cave actors actor]
  (if-let [adjacent-enemies (find-adjacent-enemies actor actors)]
    (attack actor (first adjacent-enemies))
    (move actor actors)))

(defn do-round [cave actor-registry]
  (loop [registry actor-registry
         remaining-actors (vals (keyed-by-coords (vals actor-registry)))]
    (if (empty? remaining-actors)
      registry
      (recur (take-turn cave actor-registry (first remaining-actors)) (rest remaining-actors)))))
