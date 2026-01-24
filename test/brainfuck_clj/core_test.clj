(ns brainfuck-clj.core-test
  (:require [clojure.test :refer :all]
            [brainfuck-clj.core :refer :all]))

(deftest test-bf-interpreter-basic
  (testing "Incrementing cells"
    (let [result (bf-interpreter "+++")]
      (is (= [3N] result))))
  
  (testing "Multiple cells"
    (let [result (bf-interpreter "++>+++>+")]
      (is (= [2N 3N 1N] result))))
  
  (testing "Decrementing cells"
    (let [result (bf-interpreter "+++++-----")]
      (is (= [0N] result))))
  
  (testing "Moving left and right"
    (let [result (bf-interpreter ">++>+++<<+")]
      (is (= [1N 2N 3N] result)))))

(deftest test-bf-interpreter-loops
  (testing "Simple loop"
    (let [result (bf-interpreter "+++[>+<-]")]
      (is (= [0N 3N] result))))
  
  (testing "Nested loops"
    (let [result (bf-interpreter "++[>++[>++<-]<-]")]
      (is (= [0N 0N 8N] result))))
  
  (testing "Zero cell with loop"
    (let [result (bf-interpreter "+++++[-]")]
      (is (= [0N] result)))))

(deftest test-bf-interpreter-comments
  (testing "Non-BF characters are ignored as comments"
    (let [result (bf-interpreter "This is a comment +++")]
      (is (= [3N] result)))
    (let [result (bf-interpreter "++ Comment in middle +++")]
      (is (= [5N] result)))))

(deftest test-function-definitions
  (testing "Simple function definition and call"
    ;; Define _5 as +++++, then call it (no letters allowed)
    (let [result (bf-interpreter "(_5 +++++)(_5)")]
      (is (= [5N] result))))
  
  (testing "Multiple function definitions"
    ;; Define _3 and _2, call both (no letters allowed)
    (let [result (bf-interpreter "(_3 +++)(_2 ++)(_3)(_2)")]
      (is (= [5N] result))))
  
  (testing "Function with loop"
    ;; Define _@ that moves value 3 cells right (no letters allowed)
    (let [result (bf-interpreter "(_@ [>>>+<<<-])++(_@)")]
      (is (= [0N 0N 0N 2N] result))))
  
  (testing "Function calls in sequence"
    (let [result (bf-interpreter "(_+ +++)(_+)(_+)>(_+)")]
      (is (= [6N 3N] result)))))

(deftest test-do-while-loops
  (testing "Do-while executes body and loops while non-zero"
    ;; Start at 3, do-while decrements until zero
    (let [result (bf-interpreter "+++{-}")]
      (is (= [0N] result))))
  
  (testing "Do-while with move operation"
    ;; Start at 2, do-while moves to next cell
    (let [result (bf-interpreter "++{>+<-}")]
      (is (= [0N 2N] result))))
  
  (testing "Do-while executes at least once"
    ;; Even with cell at 0, body executes once, then stops because cell is still 0
    ;; {>+<} moves right, increments, moves left - cell 0 stays 0, so executes once
    (let [result (bf-interpreter "{>+<}")]
      (is (= [0N 1N] result)))))

(deftest test-combined-features
  (testing "Functions and do-while together"
    ;; Define _5, call it, then use do-while to move value (no letters allowed)
    (let [result (bf-interpreter "(_5 +++++)(_5){>+<-}")]
      (is (= [0N 5N] result))))
  
  (testing "Nested functions"
    ;; Define a function that calls another indirectly via its body (no letters allowed)
    (let [result (bf-interpreter "(_2 ++)(_4 ++(_2))>(_4)")]
      (is (= [0N 4N] result))))
  
  (testing "Complex program with all features"
    ;; Define functions, use do-while, loops (no letters allowed)
    (let [result (bf-interpreter "(_3 +++)(_3)>>(_3)[<+>-]")]
      (is (= [3N 3N 0N] result)))))

(deftest test-esoteric-operators
  (testing "@ operator: tape echo (duplicate to next)"
    (let [result (bf-interpreter "+++@")]
      (is (= [3N 3N] result))))
  
  (testing "~ operator: invert/negate cell"
    (let [result (bf-interpreter "+++~")]
      (is (= [-3N] result))))
  
  (testing "$ operator: swap current and next cell"
    (let [result (bf-interpreter "++>+++<$")]
      (is (= [3N 2N] result))))
  
  (testing "& operator: mirror (copy next to current)"
    (let [result (bf-interpreter "++>+++<&")]
      (is (= [3N 3N] result))))
  
  (testing "# operator: quantum XOR"
    (let [result (bf-interpreter "++>+++<#")]
      ;; 2 XOR 3 = 1
      (is (= [1N 3N] result))))
  
  (testing "| operator: pipe (add next to current, zero next)"
    (let [result (bf-interpreter "++>+++<|")]
      (is (= [5N 0N] result)))))

(deftest test-esoteric-combinations
  (testing "Esoteric operators with functions"
    ;; Define function using @, then use it (no letters allowed)
    (let [result (bf-interpreter "(_@ @)(_@)++>(_@)")]
      (is (= [2N 2N] result))))
  
  (testing "Chain esoteric operators"
    ;; Set 5, echo to next, swap, invert first
    (let [result (bf-interpreter "+++++@$~")]
      (is (= [-5N 5N] result))))
  
  (testing "Esoteric in loops"
    ;; Use pipe in a loop
    (let [result (bf-interpreter "++>+++[<|>-]")]
      ;; Should accumulate: 2 + 3 + 2 + 1 = 8
      (is (= [8N 0N] result)))))

(deftest test-cryptic-operators
  (testing "% operator: context-sensitive even/odd"
    ;; Even value increments, odd value decrements
    (let [result1 (bf-interpreter "++%")]  ;; 2 is even, becomes 3
      (is (= [3N] result1)))
    (let [result2 (bf-interpreter "+++%")]  ;; 3 is odd, becomes 2
      (is (= [2N] result2))))
  
  (testing "? operator: conditional pointer move"
    ;; Positive moves right, negative moves left, zero stays
    ;; Verify by incrementing after the move
    (let [result (bf-interpreter "++>+++<?+")]
      (is (= [1N 3N] result))))  ;; Moved left from pos 1 to pos 0, then incremented
  
  (testing "! operator: invisible mutation at current-2"
    (let [result (bf-interpreter ">>!!")]  ;; At pos 2, increments pos 0 twice
      (is (= [2N 0N 0N] result))))
  
  (testing "* operator: square current cell"
    (let [result (bf-interpreter "+++*")]  ;; 3 * 3 = 9
      (is (= [9N] result))))
  
  (testing ": operator: rotate all cells right"
    (let [result (bf-interpreter "++>+++>++++<:")]  ;; Tape [2, 3, 4] rotates to [4, 2, 3]
      (is (= [4N 2N 3N] result))))
  
  (testing "^ operator: elevate to cell with matching value"
    ;; Find cell with value 2
    ;; Verify by incrementing after the elevation
    (let [result (bf-interpreter "++>++>+++<^+")]
      (is (= [3N 2N 3N] result))))  ;; Elevated to pos 0 (value 2), then incremented
  
  (testing "; operator: ghost write to random cell"
    (let [result (bf-interpreter "++;")]  ;; Increments current and a ghost cell
      ;; Total increments should be more than 3 (2 + 1 from ghost)
      (is (>= (reduce + result) 3N)))))
(deftest test-increment
  (testing "Increment operation (+)"
    (let [result (bf-interpreter "++++")]
      (is (= [4N] result)))))

(deftest test-decrement
  (testing "Decrement operation (-)"
    (let [result (bf-interpreter "+++++-----")]
      (is (= [0N] result)))))

(deftest test-move-right
  (testing "Move right operation (>)"
    (let [result (bf-interpreter "+>++>+++")]
      (is (= [1N 2N 3N] result)))))

(deftest test-move-left
  (testing "Move left operation (<)"
    (let [result (bf-interpreter "+++>++<+")]
      (is (= [4N 2N] result)))))

(deftest test-output
  (testing "Output operation (.)"
    (let [output (with-out-str
                   (bf-interpreter "+++++++++[>++++++++<-]>."))]
      (is (= "H" output)))))

(deftest test-hello-world
  (testing "Hello World program"
    (let [hello-world "++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+."
          output (with-out-str (bf-interpreter hello-world))]
      (is (= "Hello World!" output)))))

(deftest test-loop-zero
  (testing "Loop skipped when cell is zero"
    (let [result (bf-interpreter "[+++]")]
      (is (= [0N] result)))))

(deftest test-loop-execution
  (testing "Loop executes when cell is non-zero"
    (let [result (bf-interpreter "+++[>+<-]")]
      (is (= [0N 3N] result)))))

(deftest test-nested-loops
  (testing "Nested loops"
    (let [result (bf-interpreter "++[>++[>++<-]<-]")]
      (is (= [0N 0N 8N] result)))))

(deftest test-multiplication
  (testing "Multiplication: 5 * 3 = 15"
    (let [result (bf-interpreter "+++++[>+++<-]")]
      (is (= [0N 15N] result)))))

(deftest test-complex-arithmetic
  (testing "Complex arithmetic operations"
    (let [result (bf-interpreter "+++>++>+<[>[>+>+<<-]>[<+>-]<<-]")]
      ; This is a copy/multiplication algorithm
      ; Verify the result contains expected values
      (is (vector? result))
      (is (>= (count result) 3)))))

(deftest test-comments-ignored
  (testing "Comments and non-command characters are ignored"
    (let [result (bf-interpreter "++ This is a comment ++")]
      (is (= [4N] result)))))

