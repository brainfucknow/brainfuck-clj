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

;; Preprocess program to expand () function definitions and {} do-while loops
(defn preprocess [program-code]
  (let [functions (parse-functions program-code)
        expanded (expand-functions program-code functions)]
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
                nil cells
                (recur cells current-cell (inc instruction-pointer))))))

(defn -main [& args]
  (if (nth args 0)
    (bf-interpreter (slurp (nth args 0)))
    (println "Please specify a brainfuck file as the first argument")))
