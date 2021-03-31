(ns audiencerepublic.test.graph-spec
  (:require [speclj.core :refer :all]
            [audiencerepublic.graph :refer :all]))

(describe "shortest-path"
          (describe "in acyclic graph"
                    (describe "with existing path"
                              (it "should exist"
                                  (should= {:distance 4 :path [:1 :3 :4]}
                                           (shortest-path {:1 [[:2 1], [:3 2]]
                                                           :2 [[:4 4]]
                                                           :3 [[:4 2]]
                                                           :4 []}
                                                          :1 :4)))))

          (describe "with non-existent path"
                    (it "should not exist"
                        (should-be-nil (shortest-path {:1 [[:2 1], [:3 2]]
                                                       :2 [[:4 4]]
                                                       :3 [[:4 2]]
                                                       :4 []}
                                                      :4 :1))))

          (describe "in cyclic graph"
                    (describe "with existing path"
                              (it "should exist"
                                  (should= {:distance 4 :path [:1 :3 :4]}
                                           (shortest-path {:1 [[:2 1], [:3 2]]
                                                           :2 [[:4 4] [:1 1]]
                                                           :3 [[:4 2]]
                                                           :4 []}
                                                          :1 :4))))

                    (describe "with non-existent path"
                              (it "should not exist"
                                  (should-be-nil (shortest-path {:1 [[:2 1], [:3 2]]
                                                                 :2 [[:4 4] [:1 1]]
                                                                 :3 [[:4 2]]
                                                                 :4 []}
                                                                :4 :1))))))

(run-specs)
