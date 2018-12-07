(ns aoc)

(def input (map str (seq (first (clojure.string/split-lines (slurp "inputs/5.txt"))))))

(first input)

(defn abs [n] (max n (-' n)))

(defn inverse-units? [a b]
  (and (not= a b)
       (= 32 (abs (- (int a) (int b))))))


(defn react [a b]
  (cond
    (nil? a) (str b)
    (nil? b) (str a)
    :else (if (inverse-units? a b)
            ""
            (str a b))))

(= "" (react \a \A))
(= "aa" (react \a \a))
(= "x" (react nil \x))

(defn pass [polymer]
  (loop [x (str (first polymer))
         remaining (rest polymer)]
    (println "x: " x " remaining: " remaining)
    (if (<= 2 (count remaining))
      (let [product (react (last x) (first remaining))
            untouched (rest remaining)]
        (println "product: " product " empty? " (empty? product))
        (println "untouched:" untouched)
        (if (empty? product)
          (recur (str (butlast x) (first untouched)) (rest untouched) )
          (recur (str x (rest product)) untouched)))
      x)))

(pass "aAb")
(pass "abBA")
(pass "dabAcCaCBAcCcaDA") ; => dabCBAcaDA ?

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

(pass "aAb")
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
(pass "dabAcCaCBAcCcaDA") ; => dabCBAcaDA ?

(count (polymer-reaction input))
