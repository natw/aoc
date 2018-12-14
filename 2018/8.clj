(ns aoc)
(require '[clojure.zip :as z])
(use 'clojure.stacktrace)


(defn parse-line [l] (map #(Integer/parseInt %) (clojure.string/split l #" ")))
(def input-lines (clojure.string/split-lines (slurp "inputs/8.txt")))
(def input (parse-line (first input-lines)))

(def mkzip (partial z/zipper
                    (fn [n] (< 0 (n :child-count)))
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

(defn children [loc]
  (if (z/branch? loc)
    (z/children loc)
    []
    )
  )

(defn get-more-children [loc numbers]
  (let [[ccount mcount & remaining] numbers
        node (mknode ccount mcount)]
    [(z/next (add-child loc node)) remaining]))

(defn get-more-metadata [loc numbers]
  (let [meta-count ((z/node loc) :meta-count)
        metadatums (take meta-count numbers)
        remaining (nthrest numbers meta-count)]
    [(z/replace loc (assoc (z/node loc) :metadata metadatums)) remaining]

    ))

(defn back-up [loc numbers]
  [(z/up loc) numbers])

(defn look-ahead [loc remaining]
  (let [node (z/node loc)]
    (cond
      (< (count (children loc)) (node :child-count)) (get-more-children loc remaining)
      (< (count (node :metadata)) (node :meta-count)) (get-more-metadata loc remaining)
      :else (back-up loc remaining))))


(defn build-tree [numbers]
  (let [[root-children-count root-metadata-count & remaining] numbers
         root-node (mknode root-children-count root-metadata-count)
         loc' (mkzip root-node)]
    (loop [loc loc' numbers remaining]
      (if (or (z/end? loc) (empty? numbers))
        (z/root loc)
        (let [[loc' remaining'] (look-ahead loc numbers)]
          (recur loc' remaining'))))))


(def test-nums1 '(2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2))

(build-tree test-nums1)

(defn nodes [loc]
  (map z/node (take-while (complement z/end?) (iterate z/next loc))))

(reduce + (map #(reduce + %) (map :metadata (nodes (mkzip (build-tree test-nums1))))))

(reduce + (map #(reduce + %) (map :metadata (nodes (mkzip (build-tree input))))))


