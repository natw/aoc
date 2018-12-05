(ns aoc)

(def input (first (clojure.string/split-lines (slurp "inputs/5.txt"))))

(first input)

(defn abs [n] (max n (-' n)))

(defn inverse-units? [a b] 
  (and (not= a b)
       (= 32 (abs (- (int a) (int b))))))

; true
(inverse-units? \a \A)
(inverse-units? \b \B)

; false
(inverse-units? \a \a)
(inverse-units? \a \b)
(inverse-units? \a \B)


(defn react [a b]
  (cond
    (nil? a) (list b)
    (nil? b) (list a)
    :else (if (inverse-units? a b)
            '()
            (list a b))))

(react \a \A) ; =>  '()
(react \a \B) ; => '(\a \B)
(react \a nil)
(react nil \x)

(use 'clojure.tools.trace)

(conj '() '())



(defn reaction-pass [pstring]
  (apply str (if (empty? pstring)
               '()
               (if (= 1 (count pstring))
                 (seq pstring)
                 (let [[a b & remaining] pstring]
                   (let [first-reaction (react a b)]
                     (condp = (count first-reaction)
                       0 (reaction-pass remaining)
                       1 first-reaction
                       2 (cons a (reaction-pass (cons b remaining))))))))))

(reaction-pass "aAb")
(reaction-pass "aaAb")

; (defn polymer-reaction [pstring]
;   (if (<= (count pstring) 1)
;     pstring
;     (let [[u1 u2 & remaining :as all] pstring] 
;       (if (inverse-units? u1 u2)
;         (polymer-reaction remaining)
;         (polymer-reaction (cons u1 (polymer-reaction (rest all))))))))

(defn polymer-reaction [pstring]
  (loop [orig pstring
         product (reaction-pass pstring)]
    (if (< (count orig) 2)
      orig
      (if (= product orig)
        orig
        (recur product (reaction-pass product)))) ))

(polymer-reaction "aAb")
(polymer-reaction "abBA")
(polymer-reaction "dabAcCaCBAcCcaDA") ; => dabCBAcaDA ?

(count (polymer-reaction input))
