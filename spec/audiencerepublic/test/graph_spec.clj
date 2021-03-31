(ns audiencerepublic.test.graph-spec
  (:require [speclj.core :refer [describe it should-be should-be-nil should= run-specs]]
            [audiencerepublic.graph :refer [eccentricity shortest-path radius diameter make-graph]]))

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

(describe "eccentricity"
          (describe "in acyclic graph"
                    (it "should be the longest path for existing path"
                        (should= 5
                                 (eccentricity {:1 [[:2 1], [:3 2]]
                                                :2 [[:4 4]]
                                                :3 [[:4 2]]
                                                :4 []}
                                               :1)))

                    (it "should be the longest path for existing path"
                        (should= 4
                                 (eccentricity {:1 [[:2 1], [:3 2]]
                                                :2 [[:4 4]]
                                                :3 [[:4 2]]
                                                :4 []}
                                               :2)))

                    (it "should be the longest path for existing path"
                        (should= 2
                                 (eccentricity {:1 [[:2 1], [:3 2]]
                                                :2 [[:4 4]]
                                                :3 [[:4 2]]
                                                :4 []}
                                               :3)))

                    (it "should be nil for non-existing path"
                        (should-be-nil
                         (eccentricity {:1 [[:2 1], [:3 2]]
                                        :2 [[:4 4]]
                                        :3 [[:4 2]]
                                        :4 []}
                                       :4)))))

(describe "radius"
          (describe "in acyclic graph"
                    (it "should be the smallest eccentricity"
                        (should= 2
                                 (radius {:1 [[:2 1], [:3 2]]
                                          :2 [[:4 4]]
                                          :3 [[:4 2]]
                                          :4 []})))))

(describe "diameter"
          (describe "in acyclic graph"
                    (it "should be the largest eccentricity"
                        (should= 5
                                 (diameter {:1 [[:2 1], [:3 2]]
                                            :2 [[:4 4]]
                                            :3 [[:4 2]]
                                            :4 []})))))

(describe "make-graph"
          (describe "should generate a graph"
                    (it "as a map"
                        (should-be map? (make-graph 4 6)))

                    (it "with given amount of vertices"
                        (should= 4 (count (keys (make-graph 4 6)))))

                    (it "with given amount of edges"
                        (should= 7 (count (reduce-kv (fn [acc _ v] (concat acc v)) [] (make-graph 4 7)))))))

(run-specs)
