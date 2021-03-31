(ns audiencerepublic.test.graph
  (:require [clojure.test :refer [deftest testing is]]
            [audiencerepublic.graph :refer [shortest-path eccentricity radius diameter]]))

(deftest test-shortest-path
  (testing "acyclic graph"
    (testing "existing path"
      (is (= {:distance 4 :path [:1 :3 :4]}
             (shortest-path {:1 [[:2 1], [:3 2]]
                             :2 [[:4 4]]
                             :3 [[:4 2]]
                             :4 []}
                            :1 :4))))

    (testing "non-existent path"
      (is (nil?
             (shortest-path {:1 [[:2 1], [:3 2]]
                             :2 [[:4 4]]
                             :3 [[:4 2]]
                             :4 []}
                            :4 :1)))))

  (testing "cyclic graph"
    (testing "existing path"
      (is (= {:distance 4 :path [:1 :3 :4]}
             (shortest-path {:1 [[:2 1], [:3 2]]
                             :2 [[:4 4] [:1 1]]
                             :3 [[:4 2]]
                             :4 []}
                            :1 :4))))

    (testing "non-existent path"
      (is (nil?
             (shortest-path {:1 [[:2 1], [:3 2]]
                             :2 [[:4 4] [:1 1]]
                             :3 [[:4 2]]
                             :4 []}
                            :4 :1))))))
