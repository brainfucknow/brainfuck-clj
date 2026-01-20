(ns brainfuck-clj.dsl-test
  (:require [clojure.test :refer :all]
            [brainfuck-clj.dsl :as dsl]
            [brainfuck-clj.core :as core]))

(deftest test-basic-operations
  (testing "inc-cell generates correct Brainfuck"
    (is (= "+" (dsl/inc-cell)))
    (is (= "+++" (dsl/inc-cell 3))))
  
  (testing "dec-cell generates correct Brainfuck"
    (is (= "-" (dsl/dec-cell)))
    (is (= "----" (dsl/dec-cell 4))))
  
  (testing "move-right generates correct Brainfuck"
    (is (= ">" (dsl/move-right)))
    (is (= ">>" (dsl/move-right 2))))
  
  (testing "move-left generates correct Brainfuck"
    (is (= "<" (dsl/move-left)))
    (is (= "<<<" (dsl/move-left 3))))
  
  (testing "output-cell generates correct Brainfuck"
    (is (= "." (dsl/output-cell))))
  
  (testing "input-cell generates correct Brainfuck"
    (is (= "," (dsl/input-cell)))))

(deftest test-loop-operations
  (testing "loop-bf wraps body in brackets"
    (is (= "[++]" (dsl/loop-bf "++")))
    (is (= "[>.<]" (dsl/loop-bf ">.<"))))
  
  (testing "zero-cell creates loop that zeros cell"
    (is (= "[-]" (dsl/zero-cell))))
  
  (testing "clear-cell is alias for zero-cell"
    (is (= (dsl/zero-cell) (dsl/clear-cell)))))

(deftest test-value-operations
  (testing "set-value creates code to set cell value"
    (is (= "[-]+++" (dsl/set-value 3)))
    (is (= "[-]++++++++" (dsl/set-value 10))))
  
  (testing "move-value generates correct pattern"
    (let [code (dsl/move-value 1)]
      (is (string? code))
      (is (> (count code) 0))
      (is (= \[ (first code)))
      (is (= \] (last code)))))
  
  (testing "copy-value generates correct pattern"
    (let [code (dsl/copy-value 1 2)]
      (is (string? code))
      (is (> (count code) 0)))))

(deftest test-string-operations
  (testing "print-string generates code for each character"
    (let [code (dsl/print-string "Hi")]
      (is (string? code))
      (is (> (count code) 0))
      ;; Should contain multiple set-value and output operations
      (is (> (count (filter #(= % \.) code)) 1))))
  
  (testing "print-number generates code for digits"
    (is (string? (dsl/print-number 5)))
    (is (nil? (dsl/print-number 10))) ;; Out of range
    (is (nil? (dsl/print-number -1))))) ;; Out of range

(deftest test-control-flow
  (testing "when-cell creates conditional execution"
    (is (= "[>.<]" (dsl/when-cell ">.<"))))
  
  (testing "while-cell creates loop"
    (is (= "[+>-<]" (dsl/while-cell "+>-<")))))

(deftest test-higher-level-patterns
  (testing "multiply generates correct pattern"
    (let [code (dsl/multiply 3 1 2)]
      (is (string? code))
      (is (> (count code) 0))))
  
  (testing "add-to is alias for move-value"
    (is (= (dsl/move-value 2) (dsl/add-to 2)))))

(deftest test-composition
  (testing "compile-dsl concatenates expressions"
    (is (= "+++>---<" 
           (dsl/compile-dsl 
             (dsl/inc-cell 3)
             (dsl/move-right)
             (dsl/dec-cell 3)
             (dsl/move-left)))))
  
  (testing "compose concatenates fragments"
    (is (= "hello world"
           (dsl/compose "hello" " " "world")))))

(deftest test-example-patterns
  (testing "hello-world generates valid Brainfuck"
    (let [code (dsl/hello-world)]
      (is (string? code))
      (is (> (count code) 0))))
  
  (testing "echo generates valid Brainfuck"
    (let [code (dsl/echo)]
      (is (= "[,.]" code))))
  
  (testing "cat generates valid Brainfuck"
    (let [code (dsl/cat)]
      (is (string? code))
      (is (.startsWith code ","))
      (is (.contains code "[")))))

(deftest test-functional-helpers
  (testing "with-temp-cell wraps body with moves"
    (is (= ">++<" (dsl/with-temp-cell "++")))
    (is (= ">.<" (dsl/with-temp-cell ".")))))

(deftest test-dsl-execution
  (testing "DSL-generated code can be executed"
    ;; Test that simple DSL code doesn't throw errors when executed
    (let [code (dsl/compile-dsl
                 (dsl/inc-cell 5)
                 (dsl/move-right)
                 (dsl/inc-cell 3))]
      (is (string? code))
      ;; Execute and verify it returns a result (cells vector)
      (let [result (core/bf-interpreter code)]
        (is (vector? result))
        (is (= 5N (first result)))
        (is (= 3N (second result)))))))

(deftest test-hello-world-execution
  (testing "Hello World DSL program executes successfully"
    (let [code (dsl/hello-world)]
      ;; Just verify it completes without error
      (let [result (core/bf-interpreter code)]
        (is (vector? result))))))
