(ns aoc)
(require '[clojure.zip :as z])
(use 'clojure.stacktrace)


(defn parse-line [l] (map #(Integer/parseInt %) (clojure.string/split l #" ")))
(def input-lines (clojure.string/split-lines (slurp "inputs/8.txt")))
(def input (parse-line (first input-lines)))

(defn node [ccount mcount]
  {:child-count ccount
   :meta-count mcount })

(defn children [loc]
  (if (z/branch? loc)
    (z/children loc)
    '()))

(defn add-child [loc item]
  (if (z/branch? loc)
    (z/insert-child loc item)
    (z/replace loc (z/make-node loc (z/node loc) (list item)))))

(defn get-more-children [loc numbers]
  (let [[ccount mcount & remaining] numbers
        node [ccount mcount '()]]
    (println "more children")
    [(add-child loc node) remaining]))

(defn get-more-metadata [loc numbers]
  (let [meta-count (second (z/node loc))
        metadatums (take meta-count numbers)
        remaining (nthrest numbers meta-count)]
    (println "more metadata")
    [(z/replace loc (assoc (z/node loc) 2 metadatums)) remaining]))

(defn back-up [loc numbers]
  (list (z/up loc) numbers))

(defn look-ahead [loc remaining]
  (let [[child-count meta-count metadata] (z/node loc)]
    (println "look ahead")
    (println "children:" (children loc))
    (println "child-count:" child-count)
    (cond
      (< (count (children loc)) child-count) (get-more-children loc remaining)
      (< (count metadata) meta-count) (get-more-metadata loc remaining)
      :else (back-up loc remaining))))


(defn build-tree [numbers]
  (let [[root-children-count root-metadata-count & remaining] numbers
        root-node (node root-children-count root-metadata-count)
        loc (z/next)
        loc (z/next (z/seq-zip (list [root-children-count root-metadata-count]))) ]
    (loop [loc loc numbers remaining]
      (println loc)
      (if (z/end? loc)
        (z/root loc)
        (let [[loc' remaining'] (look-ahead loc numbers)]
          (println loc')
          (println remaining')
          (recur loc' remaining'))))))


(def test-nums1 '(2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2))

(z/node  (z/next (z/seq-zip (list [2 3 []]))))

(def foo (z/seq-zip (list [1] '( ([2] [3] [6]) ([4])))))
(z/next foo)

(build-tree test-nums1)

(z/children (z/seq-zip '(1 (2 3))))

(z/root (z/seq-zip '(:A (:B (:D) (:E)) (:C (:F)))))


(z/children (z/next (z/seq-zip '(( [:a :b] [:x]) [:c :d]))))
