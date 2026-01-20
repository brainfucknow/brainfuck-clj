(ns brainfuck-clj.dsl
  "Clojure-inspired DSL for Brainfuck programming.
  
  This DSL provides functional abstractions over raw Brainfuck operations,
  making Brainfuck programming more expressive and maintainable.")

;; Helper functions to generate Brainfuck code

(defn repeat-bf
  "Generate Brainfuck code that repeats a command n times."
  [command n]
  (apply str (repeat n command)))

(defn inc-cell
  "Increment current cell by n (default 1)."
  ([] "+")
  ([n] (repeat-bf \+ n)))

(defn dec-cell
  "Decrement current cell by n (default 1)."
  ([] "-")
  ([n] (repeat-bf \- n)))

(defn move-right
  "Move pointer right by n cells (default 1)."
  ([] ">")
  ([n] (repeat-bf \> n)))

(defn move-left
  "Move pointer left by n cells (default 1)."
  ([] "<")
  ([n] (repeat-bf \< n)))

(defn output-cell
  "Output current cell value as character."
  []
  ".")

(defn input-cell
  "Read input into current cell."
  []
  ",")

(defn loop-bf
  "Create a Brainfuck loop with body."
  [body]
  (str "[" body "]"))

(defn zero-cell
  "Set current cell to zero."
  []
  "[-]")

(defn clear-cell
  "Alias for zero-cell."
  []
  (zero-cell))

(defn move-value
  "Move value from current cell to cell n positions away.
  Current cell becomes 0, target cell increases by the value."
  [offset]
  (loop-bf 
    (str (dec-cell) 
         (if (pos? offset) 
           (move-right offset) 
           (move-left (- offset)))
         (inc-cell)
         (if (pos? offset)
           (move-left offset)
           (move-right (- offset))))))

(defn copy-value
  "Copy value from current cell to cell n positions away.
  Uses an additional temporary cell at position temp-offset."
  [target-offset temp-offset]
  (str
    (loop-bf
      (str (dec-cell)
           (move-right target-offset) (inc-cell) (move-left target-offset)
           (move-right temp-offset) (inc-cell) (move-left temp-offset)))
    (move-right temp-offset)
    (loop-bf
      (str (dec-cell)
           (move-left temp-offset) (inc-cell) (move-right temp-offset)))
    (move-left temp-offset)))

(defn set-value
  "Set current cell to a specific value (0-255)."
  [value]
  (str (zero-cell) (inc-cell value)))

(defn print-string
  "Generate code to print a string.
  Uses a simple approach of setting each character's value and printing."
  [s]
  (apply str
    (for [ch s]
      (str (set-value (int ch)) (output-cell)))))

(defn print-number
  "Print a single digit number (0-9)."
  [n]
  (when (and (>= n 0) (<= n 9))
    (str (set-value (+ 48 n)) (output-cell))))

;; Macro-like constructs that compile to Brainfuck

(defn when-cell
  "Execute body if current cell is non-zero.
  This is essentially just a Brainfuck loop that runs at most once."
  [body]
  (loop-bf body))

(defn while-cell
  "Execute body while current cell is non-zero.
  Standard Brainfuck loop."
  [body]
  (loop-bf body))

;; Higher-level patterns

(defn multiply
  "Multiply current cell by n, storing result in target-offset.
  Current cell becomes 0. Uses temp-offset for temporary storage."
  [n target-offset temp-offset]
  (loop-bf
    (str (dec-cell)
         (move-right target-offset)
         (inc-cell n)
         (move-left target-offset))))

(defn add-to
  "Add current cell value to cell at target-offset.
  Current cell becomes 0."
  [target-offset]
  (move-value target-offset))

;; DSL compilation

(defn compile-dsl
  "Compile DSL expressions to Brainfuck code.
  Takes a sequence of DSL function calls and returns Brainfuck string."
  [& expressions]
  (apply str expressions))

;; Example patterns for common operations

(defn hello-world
  "Generate code for 'Hello World!' program."
  []
  (print-string "Hello World!"))

(defn echo
  "Generate code that echoes input."
  []
  (loop-bf (str (input-cell) (output-cell))))

(defn cat
  "Generate code that reads and prints until EOF (0)."
  []
  (str (input-cell)
       (loop-bf (str (output-cell) (input-cell)))))

;; Functional composition

(defn compose
  "Compose multiple Brainfuck code fragments."
  [& fragments]
  (apply str fragments))

(defn with-temp-cell
  "Execute body with a temporary cell.
  Moves right to temp cell, executes body, moves back."
  [body]
  (str (move-right) body (move-left)))
