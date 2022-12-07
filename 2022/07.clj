(ns aoc
  (:require [clojure.string :as str])
  (:require [clojure.set :as set])
  (:require [clojure.zip :as zip])
  (:require [clojure.walk :as walk]))

(def lines (str/split-lines (slurp "inputs/07.txt")))

(def test1 '("$ cd /"
             "$ ls"
             "dir a"
             "14848514 b.txt"
             "8504156 c.dat"
             "dir d"
             "$ cd a"
             "$ ls"
             "dir e"
             "29116 f"
             "2557 g"
             "62596 h.lst"
             "$ cd e"
             "$ ls"
             "584 i"
             "$ cd .."
             "$ cd .."
             "$ cd d"
             "$ ls"
             "4060174 j"
             "8033020 d.log"
             "5626152 d.ext"
             "7214296 k"))

(def root-node '("/" 0 :dir))

(defn concat' [a b]
  (concat (take 3 a) b))

(def fs-zipper (zip/zipper
                (fn [n] (= :dir (nth n 2)))
                #(nthrest % 3)
                concat'
                root-node))

(def t1 (zip/seq-zip '((1 2) 0)))
(-> t1
    zip/children) ; ((1 2) 0)

(defn parse-int [ns default]
  (try
    (Integer/parseInt ns)
    (catch NumberFormatException _ default)))

(defn is-command? [line]
  (= \$ (first line)))

(defn add-dir [loc name]
  (zip/append-child loc (list name 0 :dir)))


(defn is-dir? [loc]
  (= :dir
     (nth loc 2)))

(defn loc-is-dir? [node]
  (is-dir? (zip/node node)))

(defn go-to-top' [loc]
  (loop [l loc]
    (let [up (zip/up l)]
      (if (nil? up)
        l
        (recur up)))))

(defn go-to-top [loc]
  (let [up (zip/up loc)]
    (if (nil? up)
      loc
      (go-to-top up))))

(defn cd-to [loc target]
  (cond
    (= "/" target) (go-to-top loc)
    (= ".." target) (zip/up loc)
    :else (let [sub-dirs (filter is-dir? (zip/children loc))]
            (if-let [td (some #(= target (first %)) sub-dirs)]
              loc
              (-> loc
                  (add-dir target)
                  zip/down
                  zip/rightmost)))))

(defn run-cmd [tree line]
  (let [words (str/split line #" ")]
    (cond
      (= "cd" (second words)) (cd-to tree (nth words 2))
      :else tree)))

(defn add-file [loc line]
  (let [words (str/split line #" ")
        filename (second words)
        size (parse-int (first words) 0)]
    (cond
      (= "dir" (first words)) loc ;(add-dir loc filename)
      :else (zip/append-child loc (list filename size :file)))))

(-> fs-zipper
    (cd-to "foo")
    (go-to-top')
    ; (add-file "123 hi")
    ; (add-file "123 hi")
    ; (add-file "2 second")
    ; (zip/up)
    ; (cd-to "foo")
    ; (cd-to "..")
    ; (add-file "8 ok")
    (identity))
;

(defn parse-line [loc line]
  (cond
    (is-command? line) (run-cmd loc line)
    :else (add-file loc line)))

(defn build-dir-tree [input-lines]
  (let [z fs-zipper]
    (loop [loc z
           lines input-lines]
      (if (empty? lines)
        (go-to-top' loc)
        (recur (parse-line loc (first lines))
               (rest lines))))))

(def f (-> fs-zipper
           (add-file "3 fart")
           (cd-to "asdf")
           (add-file "12 fart")
           go-to-top'))

(defn all-locs [tree]
  (->> tree
       (iterate zip/next)
       (take-while #(not (zip/end? %)))
       (remove #(nil? (first %)))))

; (defn loc-size [loc]
;   (if (loc-is-dir? loc)
;     (loop [l loc
;            size 0]
;       (if (zip/end? loc)
;         size
;         (recur (zip/next loc) (+ size (second (zip/node loc))))))
;     (second zip/node loc)))

(defn names [loc]
  (map
    (fn [l] (first (zip/node l)))
    (all-locs loc)))

(names f)


(defn total-node-size [node]
  (+ (second node) (reduce + (map total-node-size (nthrest node 3)))))

(defn dirs [loc]
  (->> (all-locs loc)
       (filter loc-is-dir?)))

(defn part1 [lines]
  (let [tree (build-dir-tree lines)
        dirs (dirs tree)
        dir-nodes (map zip/node dirs)
        sizes (map total-node-size dir-nodes)
        small-sizes (remove #(> % 100000) sizes)]
     (reduce + small-sizes)))

(part1 test1)

(part1 lines)
