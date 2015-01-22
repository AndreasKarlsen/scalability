(ns dining
  (:import (java.util.concurrent TimeUnit)
           (com.google.common.base Stopwatch))
  (:gen-class))

(def rounds (ref 200)) ;ref due to being a shared resource
(def philosophers (doall (map #(agent %) (repeat 5 0)))) ;agents due to being uncoordinated async
(def forks (doall (map #(ref [% true]) (range (count philosophers))))) ;ref due to being a shared resource
(def logger (agent 0))
(def stopwatch (. Stopwatch (createStarted)))

(def fork1 (ref true))
(def fork2 (ref true))
(def fork3 (ref true))
(def fork4 (ref true))
(def fork5 (ref true))

(defn debug [_ id msg r]
  (println id \space msg "(" r ")")
  (flush))

(defn print-stopwatch [_ time]
  (println time)
  (shutdown-agents))


(defn my-forks [id]
  (map #(nth (cycle forks) (+ (count forks) %)) [id (dec id)]))

(defn got-forks? [id]
  (every? #(= true (second (deref %))) (my-forks id)))

(defn handle-forks [id action]
  (doseq [fork (my-forks id)]
    (ref-set fork [(first @fork) (condp = action :take false :release true)])))

(defn more-food? []
  (if (pos? (ensure rounds))
    true
    (do
      (. stopwatch (stop))
      (send logger print-stopwatch (. stopwatch (elapsed (. TimeUnit MILLISECONDS))))
      false)))

(defn behave [id left right]
  (loop []
    (dosync (more-food?))
    (dosync ; Initiate transaction
        (when (and @left @right) ; Are both of my forks available?
          (do
            (ref-set left false)
            (ref-set right false)
            (Thread/sleep 100)
            ;(handle-forks id :take)
            (commute rounds dec)
            (send-off logger debug id "ate      " @rounds)
            ;(handle-forks id :release)
            (ref-set left true)
            (ref-set right true))
          ))
    (Thread/sleep 100)
    (recur))) ; Repeat above

(defn start []
  (do
    (future (behave 0 fork1 fork2))
    (future (behave 1 fork2 fork3))
    (future (behave 2 fork3 fork4))
    (future (behave 3 fork4 fork5))
    (future (behave 4 fork5 fork1))))


(defn -main [& args]
  (start))