(defproject audiencerepublic "0.1.0-SNAPSHOT"
  :description "This is a solution for audiencerepublic test"
  :url "https://bitbucket.org/audiencerepublic/developer-test/wiki/clojure-2"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [speclj "3.3.2"]]
  :profiles {:dev {:dependencies [[speclj "3.3.2"]]}}
  :plugins [[speclj "3.3.2"]]
  :test-paths ["spec"]
  :repl-options {:init-ns audiencerepublic.graph})
