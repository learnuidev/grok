(ns grok.core-test
  (:require [clojure.test :refer [deftest testing is]]))

(deftest sample
  (testing "1 + 1 = 2"
    (is (= 2 (+ 1 1)))))
