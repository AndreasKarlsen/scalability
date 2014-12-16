(defproject kmeans "1.0.0-SNAPSHOT"
  :description "Kmeans performance test in clojure"
  :url "https://github.com/Felorati/scalability"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.google.guava/guava "18.0"]]
  :source-paths      ["src/main/clojure"]
  :java-source-paths ["src/main/java"]
  :javac-options ["-target" "1.8" "-source" "1.8"]
  :main hello)