(ns brainfuck-clj.core
    (:gen-class))

;; Expand function calls in a string recursively
(defn expand-calls [code functions]
  (loop [i 0
         result ""]
    (if (>= i (count code))
      result
      (if (= \( (nth code i))
        (let [close-paren (loop [j (inc i) depth 1]
                            (cond
                              (>= j (count code)) j
                              (= \( (nth code j)) (recur (inc j) (inc depth))
                              (= \) (nth code j)) (if (= depth 1) 
                                                     j 
                                                     (recur (inc j) (dec depth)))
                              :else (recur (inc j) depth)))
              content (subs code (inc i) close-paren)
              trimmed (clojure.string/trim content)]
          ;; If it's just a name and exists in functions, expand it
          (if (and (not (.contains trimmed " "))
                   (contains? functions trimmed))
            (recur (inc close-paren) (str result (get functions trimmed)))
            (recur (inc close-paren) result)))
        (recur (inc i) (str result (nth code i)))))))

;; Parse function definitions from program code
;; Format: (name body) defines a function named 'name' with code 'body'
(defn parse-functions [program-code]
  (loop [i 0
         functions {}]
    (if (>= i (count program-code))
      ;; After parsing all functions, expand calls in function bodies
      (into {} (for [[name body] functions]
                 [name (expand-calls body functions)]))
      (if (= \( (nth program-code i))
        ;; Found opening paren, parse function definition
        (let [close-paren (loop [j (inc i) depth 1]
                            (cond
                              (>= j (count program-code)) j
                              (= \( (nth program-code j)) (recur (inc j) (inc depth))
                              (= \) (nth program-code j)) (if (= depth 1) 
                                                             j 
                                                             (recur (inc j) (dec depth)))
                              :else (recur (inc j) depth)))
              content (subs program-code (inc i) close-paren)
              trimmed (clojure.string/trim content)]
          ;; Check if it's a definition (contains space) or call (no space)
          (if (.contains trimmed " ")
            ;; It's a definition: parse name and body
            (let [space-idx (.indexOf trimmed " ")
                  func-name (subs trimmed 0 space-idx)
                  func-body (clojure.string/trim (subs trimmed (inc space-idx)))]
              (recur (inc close-paren) (assoc functions func-name func-body)))
            ;; It's a call or empty, skip it
            (recur (inc close-paren) functions)))
        (recur (inc i) functions)))))

;; Expand function calls in program code
;; Replace (name) with the function body, remove (name body) definitions
(defn expand-functions [program-code functions]
  (expand-calls program-code functions))

;; Remove all letter characters from the code
(defn strip-letters [code]
  (apply str (filter #(not (Character/isLetter %)) code)))

;; Preprocess program to expand () function definitions and {} do-while loops
(defn preprocess [program-code]
  (let [no-letters (strip-letters program-code)
        functions (parse-functions no-letters)
        expanded (expand-functions no-letters functions)]
    ;; Now handle {} do-while loops
    ;; { body } becomes: body [ body ]
    (loop [i 0
           result ""]
      (if (>= i (count expanded))
        result
        (if (= \{ (nth expanded i))
          ;; Found opening brace, parse do-while body
          (let [close-brace (loop [j (inc i) depth 1]
                              (cond
                                (>= j (count expanded)) j
                                (= \{ (nth expanded j)) (recur (inc j) (inc depth))
                                (= \} (nth expanded j)) (if (= depth 1) 
                                                           j 
                                                           (recur (inc j) (dec depth)))
                                :else (recur (inc j) depth)))
                body (subs expanded (inc i) close-brace)
                ;; Do-while: execute body, then loop while cell is non-zero
                do-while (str body "[" body "]")]
            (recur (inc close-brace) (str result do-while)))
          (recur (inc i) (str result (nth expanded i))))))))

(defn bf-interpreter [program-code]
    (let [
        processed-code (preprocess program-code)
        find-bracket (fn [opening-bracket closing-bracket instruction-pointer direction]
            (loop [i (direction instruction-pointer) opened 0]
                (condp = (nth processed-code i)
                    opening-bracket (recur (direction i) (inc opened))
                    closing-bracket (if (zero? opened) i (recur (direction i) (dec opened)))
                    (recur (direction i) opened))))]

        (loop [cells [0N], current-cell 0, instruction-pointer 0]
            (condp = (get processed-code instruction-pointer)
                \<  (recur cells (dec current-cell) (inc instruction-pointer))
                \+  (recur (update-in cells [current-cell] inc) current-cell (inc instruction-pointer))
                \-  (recur (update-in cells [current-cell] dec) current-cell (inc instruction-pointer))

                \>  (let [
                            next-ptr (inc current-cell)
                            next-cells (if (= next-ptr (count cells)) 
                                            (conj cells 0N) 
                                             cells)]
                            (recur next-cells next-ptr (inc instruction-pointer)))

                \.  (do
                        (print (char (nth cells current-cell)))
                        (recur cells current-cell (inc instruction-pointer)))
                \,  (let [ch (.read System/in)]
                        (recur (assoc cells current-cell ch) current-cell (inc instruction-pointer)))
                \[  (recur cells current-cell (inc (if (zero? (nth cells current-cell))
                        (find-bracket \[ \] instruction-pointer inc)
                        instruction-pointer)))
                \]  (recur cells current-cell (find-bracket \] \[ instruction-pointer dec))
                
                ;; Esoteric operators
                \@  ;; Tape echo: duplicate current cell to next cell
                    (let [next-ptr (inc current-cell)
                          next-cells (if (= next-ptr (count cells)) 
                                       (conj cells 0N) 
                                       cells)
                          value (nth next-cells current-cell)
                          result-cells (assoc next-cells next-ptr value)]
                      (recur result-cells current-cell (inc instruction-pointer)))
                
                \~  ;; Invert: negate current cell (two's complement style)
                    (recur (update-in cells [current-cell] #(- (bigint %))) 
                           current-cell 
                           (inc instruction-pointer))
                
                \$  ;; Swap: swap current and next cell
                    (let [next-ptr (inc current-cell)
                          next-cells (if (= next-ptr (count cells)) 
                                       (conj cells 0N) 
                                       cells)
                          curr-val (nth next-cells current-cell)
                          next-val (nth next-cells next-ptr)
                          swapped (-> next-cells
                                     (assoc current-cell next-val)
                                     (assoc next-ptr curr-val))]
                      (recur swapped current-cell (inc instruction-pointer)))
                
                \&  ;; Mirror: copy next cell to current
                    (let [next-ptr (inc current-cell)
                          next-cells (if (= next-ptr (count cells)) 
                                       (conj cells 0N) 
                                       cells)
                          next-val (nth next-cells next-ptr)]
                      (recur (assoc next-cells current-cell next-val) 
                             current-cell 
                             (inc instruction-pointer)))
                
                \#  ;; Quantum: XOR current with next cell
                    (let [next-ptr (inc current-cell)
                          next-cells (if (= next-ptr (count cells)) 
                                       (conj cells 0N) 
                                       cells)
                          curr-val (nth next-cells current-cell)
                          next-val (nth next-cells next-ptr)
                          ;; Handle XOR for bigints safely
                          xor-result (bigint (bit-xor (mod (.longValue curr-val) 256)
                                                      (mod (.longValue next-val) 256)))]
                      (recur (assoc next-cells current-cell xor-result) 
                             current-cell 
                             (inc instruction-pointer)))
                
                \|  ;; Pipe: add next to current, zero next
                    (let [next-ptr (inc current-cell)
                          next-cells (if (= next-ptr (count cells)) 
                                       (conj cells 0N) 
                                       cells)
                          curr-val (nth next-cells current-cell)
                          next-val (nth next-cells next-ptr)
                          piped (-> next-cells
                                   (assoc current-cell (+ curr-val next-val))
                                   (assoc next-ptr 0N))]
                      (recur piped current-cell (inc instruction-pointer)))
                
                ;; Cryptic unreadable operators
                \%  ;; Context-sensitive: inc if even, dec if odd
                    (let [value (nth cells current-cell)
                          even? (zero? (mod (.longValue value) 2))
                          new-cells (if even?
                                      (update-in cells [current-cell] inc)
                                      (update-in cells [current-cell] dec))]
                      (recur new-cells current-cell (inc instruction-pointer)))
                
                \?  ;; Conditional pointer move based on cell value
                    (let [value (nth cells current-cell)
                          new-ptr (cond
                                   (pos? value) (inc current-cell)
                                   (neg? value) (max 0 (dec current-cell))
                                   :else current-cell)
                          new-cells (if (and (pos? value) (= new-ptr (count cells)))
                                      (conj cells 0N)
                                      cells)]
                      (recur new-cells new-ptr (inc instruction-pointer)))
                
                \!  ;; Invisible mutation: affects cell at current-2 silently
                    (let [target (max 0 (- current-cell 2))
                          new-cells (update-in cells [target] inc)]
                      (recur new-cells current-cell (inc instruction-pointer)))
                
                \*  ;; Square current cell (multiply by itself)
                    (let [value (nth cells current-cell)
                          squared (* value value)]
                      (recur (assoc cells current-cell squared) 
                             current-cell 
                             (inc instruction-pointer)))
                
                \:  ;; Rotate: shift all values right, wrap last to first
                    (let [rotated (if (> (count cells) 1)
                                    (vec (cons (last cells) (butlast cells)))
                                    cells)]
                      (recur rotated current-cell (inc instruction-pointer)))
                
                \^  ;; Elevate: move pointer to cell with value = current cell value
                    (let [target-value (nth cells current-cell)
                          target-index (or (first (keep-indexed 
                                                   #(when (and (not= %1 current-cell)
                                                              (= %2 target-value)) 
                                                      %1) 
                                                   cells))
                                          current-cell)]
                      (recur cells target-index (inc instruction-pointer)))
                
                \\;  ;; Ghost write: write to current AND a pseudo-random cell
                     (let [ghost-cell (mod (* (+ current-cell 
                                                 (mod (.longValue (nth cells current-cell)) 100)) 
                                             7) 
                                          (count cells))
                           new-cells (-> cells
                                       (update-in [current-cell] inc)
                                       (update-in [ghost-cell] inc))]
                       (recur new-cells current-cell (inc instruction-pointer)))
                
                nil cells
                (recur cells current-cell (inc instruction-pointer))))))

(defn -main [& args]
  (if (nth args 0)
    (bf-interpreter (slurp (nth args 0)))
    (println "Please specify a brainfuck file as the first argument")))
