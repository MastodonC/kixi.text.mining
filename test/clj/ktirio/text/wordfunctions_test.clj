(ns ktirio.text.wordfunctions-test
  (:require [clojure.test :refer :all]
            [ktirio.text.wordfunctions :refer :all]))

(deftest word-length-test
  (testing "Word length test (longer than x but less than y): FAILS"
    (is (= true (word-length-valid "intelligent" 3 20)))
    (is (= false (word-length-valid "in" 3 20)))))

(deftest paragraph-length-test
  (testing "Minimum paragraph length (in words): FAILS"
    (is (= true (min-paragraph-length-valid "this is a paragraph" 2)))
    (is (= false (min-paragraph-length-valid "this is a paragraph" 7)))))

(deftest remove-numbers-test
  (testing "Removing numbers from text"
    (is (= "a  b  c" (remove-numbers "a 1 b 2 c")))
    (is (= "abc" (remove-numbers "a1b2c3")))))

(deftest remove-punctuation-test
  (testing "Removing punctuation from text"
    (is (= "There s a magic place we re on our way there" (remove-punctuation "There's a magic place, we're on our way there")))))
