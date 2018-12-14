(ns aoc)
(require '[clojure.zip :as z])
(use 'clojure.stacktrace)


(defn parse-line [l] (map #(Integer/parseInt %) (clojure.string/split l #" ")))
(def input-lines (clojure.string/split-lines (slurp "inputs/8.txt")))
(def input (parse-line (first input-lines)))

(def mkzip (partial z/zipper
                    (fn [n] true)
                    (fn [n] (n :children))
                    (fn [n cs] (assoc n :children cs))))

(defn mknode [ccount mcount]
  {:child-count ccount
   :meta-count mcount
   :metadata []
   :children []})

(defn add-child [loc item]
  (z/insert-child loc item)
  )

(defn get-more-children [loc numbers]
  (let [[ccount mcount & remaining] numbers
        node (mknode ccount mcount)]
    (println "--------")
    (println "adding another child")
    (println "assigning:" node)
    (println "passing on:" remaining)
    [(z/next (add-child loc node)) remaining]))

(defn get-more-metadata [loc numbers]
  (let [meta-count ((z/node loc) :meta-count)
        metadatums (take meta-count numbers)
        remaining (nthrest numbers meta-count)]
    (println "--------")
    (println "more metadata")
    (println "assigning:" metadatums)
    (println "passing on:" remaining)
    [(z/replace loc (assoc (z/node loc) :metadata metadatums)) remaining]

    ))

(defn back-up [loc numbers]
  [(z/up loc) numbers])

(defn look-ahead [loc remaining]
  (let [node (z/node loc)]
    (println "--------")
    (println "look ahead")
    (println "")
    (println "children:" (z/children loc))
    (println "child-count:" (node :child-count))
    ; (println (get-more-children loc remaining))
    (println "")
    (println "metadata:" (node :metadata))
    (println "meta-count:" (node :meta-count))
    ; (println (get-more-metadata loc remaining))

    (println "backup:" (back-up loc remaining))
    (cond
      (< (count (z/children loc)) (node :child-count)) (get-more-children loc remaining)
      (< (count (node :metadata)) (node :meta-count)) (get-more-metadata loc remaining)
      :else (back-up loc remaining))))


(defn build-tree [numbers]
  (let [[root-children-count root-metadata-count & remaining] numbers
         root-node (mknode root-children-count root-metadata-count)
         loc' (mkzip root-node)]
    (loop [loc loc' numbers remaining]
      (println "#######################")
      (println "another loop:")
      (println loc)
      (println numbers)
      (if (or (z/end? loc) (empty? numbers))
        (z/root loc)
        (let [[loc' remaining'] (look-ahead loc numbers)]
          (println "about to recur with:")
          (println loc')
          (println remaining')
          (recur loc' remaining'))))))


(def test-nums1 '(2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2))

(build-tree test-nums1)


(println (cond
  true "what"
  true "ok"
  ))
