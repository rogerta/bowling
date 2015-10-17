(ns bowling.core-test
  (:require [clojure.test :refer :all]
            [bowling.core :refer :all]))


(deftest test-is-valid?
  (testing "with empty list"
    (is (true? (is-valid? ()))))
  (testing "with one valid"
    (is (true? (is-valid? '(1)))))
  (testing "with one invalid"
    (is (false? (is-valid? '(11)))))
  (testing "with two valid"
    (is (true? (is-valid? '(1 2)))))
  (testing "with two rolls that add up to more than 10"
    (is (false? (is-valid? '(6 7)))))
  (testing "with perfect game"
    (is (true? (is-valid? '(10 10 10 10 10 10 10 10 10 10 10 10)))))
  (testing "too many rolls"
    (is (false? (is-valid? '(1 1 2 2 3 3 4 4 5 5 1 1 2 2 3 3 4 4 5 5 1 1)))))
  )


(deftest test-frame-score
  (testing "with empty list"
    (is (= 0 (frame-score '()))))
  (testing "with one roll"
    (is (= 1 (frame-score '(1)))))
  (testing "with two rolls"
    (is (= 3 (frame-score '(1 2)))))
  (testing "spare, nothing after"
    (is (= 10 (frame-score '(3 7)))))
  (testing "spare, one after"
    (is (= 12 (frame-score '(3 7 2)))))
  (testing "spare, two after"
    (is (= 12 (frame-score '(3 7 2 4)))))
  (testing "srike, nothing after"
    (is (= 10 (frame-score '(10)))))
  (testing "srike, one after"
    (is (= 12 (frame-score '(10 2)))))
  (testing "srike, two after"
    (is (= 16 (frame-score '(10 2 4)))))
  (testing "srike, three after"
    (is (= 16 (frame-score '(10 2 4 7)))))
  )

