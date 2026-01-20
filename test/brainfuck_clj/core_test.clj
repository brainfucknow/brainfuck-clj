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
    ;; Define inc5 as +++++, then call it
    (let [result (bf-interpreter "(inc5 +++++)(inc5)")]
      (is (= [5N] result))))
  
  (testing "Multiple function definitions"
    ;; Define inc3 and inc2, call both
    (let [result (bf-interpreter "(inc3 +++)(inc2 ++)(inc3)(inc2)")]
      (is (= [5N] result))))
  
  (testing "Function with loop"
    ;; Define move3 that moves value 3 cells right
    (let [result (bf-interpreter "(move3 [>>>+<<<-])++(move3)")]
      (is (= [0N 0N 0N 2N] result))))
  
  (testing "Function calls in sequence"
    (let [result (bf-interpreter "(inc +++)(inc)(inc)>(inc)")]
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
    ;; Define inc5, call it, then use do-while to move value
    (let [result (bf-interpreter "(inc5 +++++)(inc5){>+<-}")]
      (is (= [0N 5N] result))))
  
  (testing "Nested functions"
    ;; Define a function that calls another indirectly via its body
    (let [result (bf-interpreter "(add2 ++)(add4 ++(add2))>(add4)")]
      (is (= [0N 4N] result))))
  
  (testing "Complex program with all features"
    ;; Define functions, use do-while, loops
    (let [result (bf-interpreter "(set3 +++)(set3)>>(set3)[<+>-]")]
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
    ;; Define echo function using @, then use it
    (let [result (bf-interpreter "(echo @)(echo)++>(echo)")]
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
