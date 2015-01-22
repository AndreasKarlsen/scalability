(ns dining
  (:import (java.util.concurrent TimeUnit)
           (com.google.common.base Stopwatch))
  (:gen-class))

(def rounds (ref 20)) ;ref due to being a shared resource
(def philosophers (doall (map #(agent %) (repeat 5 0)))) ;agents due to being uncoordinated async
(def forks (doall (map #(ref [% true]) (range (count philosophers))))) ;ref due to being a shared resource
(def logger (agent 0))
(def stopwatch (. Stopwatch (createStarted)))

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

(defn behave [a id]
  (dosync ; Initiate transaction
    (when (more-food?) ; Is there more food?
      ;(if (> 5 (rand-int 10))        ; Do I want to takeFood or think?
      (when (got-forks? id) ; Are both of my forks available?
        (do
          (handle-forks id :take)
          (alter rounds dec)
          (Thread/sleep 100)
          ;(send-off logger debug id "ate      " @rounds)
          (println (str id " ate " @rounds))
          (handle-forks id :release)))))
  ;(Thread/sleep 100)
  (send-off *agent* behave id)) ; Repeat above

(defn start []
  (doseq [i (range (count philosophers))]
    (send logger debug i "being sent off to dinner" @rounds)
    (send-off (nth philosophers i) behave i)))


(defn -main [& args]
  (start))