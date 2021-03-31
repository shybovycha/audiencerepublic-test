(ns audiencerepublic.graph)

(defn- reconstruct-path
  "Reconstructs a path from a dijkstra representation to a sequence of vertices"
  ([start end distances previous] (reconstruct-path start end previous [end] (get distances end)))

  ([start end previous path distance]
   (when-not (nil? end)
     (if (= end start)
       { :path (into [] (reverse path)) :distance distance}
       (let [through (get previous end)]
         (recur start through previous (conj path through) distance))))))

(defn- shortest-path-impl
  "Calculates the shortest path and its distance in a graph using Dijkstra algorithm"
  ([graph start end]
   (let [vertices (conj (into [] (remove #{start} (keys graph))) start)
         distance (assoc (reduce (fn [acc vertex] (assoc acc vertex Integer/MAX_VALUE)) {} vertices) start 0)
         previous (reduce (fn [acc v] (assoc acc v nil)) {} vertices)]
     (shortest-path-impl graph start end vertices #{} distance previous)))

  ([graph start end queue visited distance previous]
   (cond
     (empty? queue) (reconstruct-path start end distance previous)
     (contains? visited (peek queue)) (recur graph start end (pop queue) visited distance previous)
     :else (let [u (peek queue)
                 neighbour-edges (get graph u)
                 [new-distance new-previous] (reduce
                                              (fn [[distance1 previous1] [v length]]
                                                (let [dist-u (get distance1 u)
                                                      dist-v (get distance1 v)
                                                      alt (+ dist-u length)]
                                                  (if (< alt dist-v) [(assoc distance1 v alt) (assoc previous1 v u)] [distance1 previous1])))
                                              [distance previous]
                                              neighbour-edges)]
             (recur graph start end (pop queue) (conj visited u) new-distance new-previous)))))

(defn complementary-graph [graph] (reduce-kv (fn [acc from vs] (assoc acc from (map (fn [[to length]] [to (- length)]) vs))) {} graph))

(defn- longest-path-impl
  "Calculates the longest path and its distance in a graph using Dijkstra algorithm on a complementary graph"
  [graph start end]
   (let [r (shortest-path-impl (complementary-graph graph) start end)]
     (when-not (nil? r) (update r :distance -))))

(defn- product
  "Creates a cartesian product of two collections"
  ([list1] (product list1 list1 []))
  ([list1 list2] (product list1 list2 []))
  ([list1 list2 acc]
   (if (empty? list1)
     acc
     (recur (rest list1) list2 (concat acc (map #(conj [(first list1)] %) list2))))))

(defn- edges-to-adjacency
  "Converts a graph represented as edge list to same graph represented as adjacency list"
  [edges]
  (reduce
   (fn [acc [from to length]]
     (->
      (if (nil? (get acc to)) (assoc acc to []) acc)
      (assoc from (conj (get acc from) [to length]))))
   {}
   edges))

(defn graph-to-graphviz [G]
  (apply str (reduce-kv (fn [acc from vs] (concat acc (map (fn [[to length]] (str (name from) "->" (name to) " [label=\"" length "\"];")) vs))) [] G)))

(def to-keyword (comp keyword str))

(def G {:1 [[:2 1], [:3 2]]
        :2 [[:4 4]]
        :3 [[:4 2]]
        :4 []})

(def shortest-path (memoize shortest-path-impl))

(def longest-path (memoize longest-path-impl))

(defn make-graph
  "Generates a random oriented weighted graph using cartesian product of vertices"
  [n s]
  (when-not (or (< s (dec n)) (> s (* n (dec n))))
    (let [vertices (map to-keyword (range 1 (inc n)))
          all-edges (map (fn [[from to]] [from to]) (product vertices))
          no-loop-edges (filter (fn [[from to]] (not (= from to))) all-edges)
          skeleton (map (fn [v] (first (filter (fn [[from]] (= from v)) no-loop-edges))) vertices)
          extras (take (- s n) (shuffle (remove (set skeleton) no-loop-edges)))
          edges (map #(conj % (inc (rand-int 10))) (concat skeleton extras))]
      (edges-to-adjacency edges))))

(defn make-graph-2
  "Generates a random oriented weighted graph using recursive approach"
  ([n s]
   (when-not (or (< s (dec n)) (> s (* n (dec n))))
     (let [vertices (into [] (map to-keyword (range 1 (inc n))))]
       (edges-to-adjacency (make-graph-2 vertices vertices [] s)))))

  ([vertices queue acc n]
   (cond
     (= n 0) acc
     (empty? queue) (recur vertices (into [] (map second acc)) acc n)
     :else (let [from (peek queue)
                 remaining-vertices (remove #{from} vertices)
                 to (rand-nth remaining-vertices)
                 length (rand-int 10)]
             (recur vertices (pop queue) (cons [from to length] acc) (dec n))))))

(defn eccentricity
  "The greatest distance between v and any other vertex in graph"
  [graph v]
  (let [vertices (remove #{v} (keys graph))
        longest-distances (remove nil? (map (fn [u] (:distance (longest-path graph v u))) vertices))]
    (when-not (empty? longest-distances) (apply max longest-distances))))

(defn- distance-fn [function graph]
  (let [eccentrities (remove nil? (map (fn [v] (eccentricity graph v)) (keys graph)))]
    (when-not (empty? eccentrities) (apply function eccentrities))))

(defn radius [graph] (distance-fn min graph))

(defn diameter [graph] (distance-fn max graph))
