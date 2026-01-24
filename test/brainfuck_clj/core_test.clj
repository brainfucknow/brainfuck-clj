(ns brainfuck-clj.core-test
  (:require [clojure.test :refer :all]
            [brainfuck-clj.core :refer :all]))

(deftest test-increment
  (testing "Increment operation (+)"
    (with-out-str
      (let [result (bf-interpreter "++++")]
        (is (= [4N] result))))))

(deftest test-decrement
  (testing "Decrement operation (-)"
    (with-out-str
      (let [result (bf-interpreter "+++++-----")]
        (is (= [0N] result))))))

(deftest test-move-right
  (testing "Move right operation (>)"
    (with-out-str
      (let [result (bf-interpreter "+>++>+++")]
        (is (= [1N 2N 3N] result))))))

(deftest test-move-left
  (testing "Move left operation (<)"
    (with-out-str
      (let [result (bf-interpreter "+++>++<+")]
        (is (= [4N 2N] result))))))

(deftest test-output
  (testing "Output operation (.)"
    (let [output (with-out-str
                   (bf-interpreter "+++++++++[>++++++++<-]>+."))]
      (is (= "H" output)))))

(deftest test-hello-world
  (testing "Hello World program"
    (let [hello-world "++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+."
          output (with-out-str (bf-interpreter hello-world))]
      (is (= "Hello World!\n" output)))))

(deftest test-loop-zero
  (testing "Loop skipped when cell is zero"
    (with-out-str
      (let [result (bf-interpreter "[+++]")]
        (is (= [0N] result))))))

(deftest test-loop-execution
  (testing "Loop executes when cell is non-zero"
    (with-out-str
      (let [result (bf-interpreter "+++[>+<-]")]
        (is (= [0N 3N] result))))))

(deftest test-nested-loops
  (testing "Nested loops"
    (with-out-str
      (let [result (bf-interpreter "++[>++[>++<-]<-]")]
        (is (= [0N 0N 8N] result))))))

(deftest test-multiplication
  (testing "Multiplication: 5 * 3 = 15"
    (with-out-str
      (let [result (bf-interpreter "+++++[>+++<-]")]
        (is (= [0N 15N] result))))))

(deftest test-complex-arithmetic
  (testing "Complex arithmetic operations"
    (with-out-str
      (let [result (bf-interpreter "+++>++>+<[>[>+>+<<-]>[<+>-]<<-]")]
        (is (= 6N (nth result 2)))))))

(deftest test-comments-ignored
  (testing "Comments and non-command characters are ignored"
    (with-out-str
      (let [result (bf-interpreter "++ This is a comment ++")]
        (is (= [4N] result))))))

