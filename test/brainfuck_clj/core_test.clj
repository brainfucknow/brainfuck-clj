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
      (is (= [5N] result))))))

