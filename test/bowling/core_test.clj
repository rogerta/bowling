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
  (testing "with one negative"
    (is (false? (is-valid? '(-1)))))
  (testing "with two valid"
    (is (true? (is-valid? '(1 2)))))
  (testing "with two rolls that add up to more than 10"
    (is (false? (is-valid? '(6 7)))))
  (testing "with perfect game"
    (is (true? (is-valid? '(10 10 10 10 10 10 10 10 10 10 10 10)))))
  (testing "with too many rolls"
    (is (false? (is-valid? '(1 1 2 2 3 3 4 4 5 5 1 1 2 2 3 3 4 4 5 5 1 1)))))
  )


(deftest test-frame-score
  (testing "with empty list"
    (is (= 0 (frame-score '()))))
  (testing "with one roll"
    (is (= 1 (frame-score '(1)))))
  (testing "with two rolls"
    (is (= 3 (frame-score '(1 2)))))
  (testing "with spare, nothing after"
    (is (= 10 (frame-score '(3 7)))))
  (testing "with spare, one after"
    (is (= 12 (frame-score '(3 7 2)))))
  (testing "with spare, two after"
    (is (= 12 (frame-score '(3 7 2 4)))))
  (testing "with strike, nothing after"
    (is (= 10 (frame-score '(10)))))
  (testing "with strike, one after"
    (is (= 12 (frame-score '(10 2)))))
  (testing "with strike, two after"
    (is (= 16 (frame-score '(10 2 4)))))
  (testing "with strike, three after"
    (is (= 16 (frame-score '(10 2 4 7)))))
  )


(deftest test-build-frames
  (testing "with empty list"
    (is (empty? (build-frames ()))))
  (testing "with one roll"
    (is (= (list (struct Frame 1 nil 1)) (build-frames '(1)))))
  (testing "with two rolls"
    (is (= (list (struct Frame 1 2 3)) (build-frames '(1 2)))))
  (testing "with three rolls"
    (is (= (list (struct Frame 1 2 3) (struct Frame 5 nil 5))
           (build-frames '(1 2 5)))))
  (testing "with spare, one after"
    (is (= (list (struct Frame 3 7 12) (struct Frame 2 nil 2))
           (build-frames '(3 7 2)))))
  (testing "with spare, two after"
    (is (= (list (struct Frame 3 7 12) (struct Frame 2 4 6))
           (build-frames '(3 7 2 4)))))
  (testing "with strike, one after"
    (is (= (list (struct Frame 10 nil 12) (struct Frame 2 nil 2))
           (build-frames '(10 2)))))
  (testing "with strike, two after"
    (is (= (list (struct Frame 10 nil 16) (struct Frame 2 4 6))
           (build-frames '(10 2 4)))))
  (testing "with strike, spare, one after"
    (is (= (list (struct Frame 10 nil 20)
                 (struct Frame 3 7 14)
                 (struct Frame 4 nil 4))
           (build-frames '(10 3 7 4)))))
  (testing "with strike, strike, one after"
    (is (= (list (struct Frame 10 nil 24)
                 (struct Frame 10 nil 14)
                 (struct Frame 4 nil 4))
           (build-frames '(10 10 4)))))
  (testing "with strike, strike, strike"
    (is (= (list (struct Frame 10 nil 30)
                 (struct Frame 10 nil 20)
                 (struct Frame 10 nil 10))
           (build-frames '(10 10 10)))))
  )


(deftest test-game-over?
  (testing "with empty list"
    (is (false? (game-over? ()))))
  (testing "with full frames"
    (is (false? (game-over? (build-frames '(1 2 3 4))))))
  (testing "with partial frame"
    (is (false? (game-over? (build-frames '(1 2 3 4 5))))))
  (testing "with regular last frame"
    (is (true? (game-over? (build-frames
                              '(0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 2))))))
  (testing "with spake, no 11th"
    (is (false? (game-over? (build-frames
                               '(0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 3 7))))))
  (testing "with spake, with partial 11th"
    (is (true? (game-over? (build-frames
                              '(0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 3 7 0))))))
  (testing "with strike, no 11th"
    (is (false? (game-over? (build-frames
                              '(0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 10))))))
  (testing "with strike, with partial 11th"
    (is (false? (game-over? (build-frames
                              '(0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 10 0))))))
  (testing "with strike, with full 11th"
    (is (true? (game-over? (build-frames
                             '(0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 10 0 0))))))
  )

