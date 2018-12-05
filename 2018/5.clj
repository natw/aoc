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
  (if (inverse-units? a b)
    '()
    (list a b)))

(react \a \A) ; =>  '()
(react \a \B) ; => '(\a \B)

(use 'clojure.tools.trace)

(defn reaction-pass [pstring]
  (reduce react pstring))


; (defn polymer-reaction [pstring]
;   (if (<= (count pstring) 1)
;     pstring
;     (let [[u1 u2 & remaining :as all] pstring] 
;       (if (inverse-units? u1 u2)
;         (polymer-reaction remaining)
;         (polymer-reaction (cons u1 (polymer-reaction (rest all))))))))

(defn polymer-reaction [pstring]
  (println "starting" pstring)
  (loop [product (reaction-pass pstring)]
    (println "what up" product)
    (if (<= (count pstring) 2)
      pstring
      (if (= product pstring)
        pstring
        (recur (reaction-pass pstring)))) ))

(polymer-reaction "aAb")
(polymer-reaction "abBA")
(polymer-reaction "dabAcCaCBAcCcaDA") ; => dabCBAcaDA ?

