(ns diningWithCount
  (:import (java.util.concurrent TimeUnit)
           (com.google.common.base Stopwatch))
  (:gen-class))

(defmacro spy-dosync [& body]
  `(let [retries# (atom -1)
         result# (dosync
                   (swap! retries# inc)
                   ~@body)]
     (send-off logger debug "" "retries count:" @retries#)
     result#))

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

(defn behave [a id left right]
  (dosync ; Initiate transaction
    (when (more-food?) ; Is there more food?
      ;(if (> 5 (rand-int 10))        ; Do I want to takeFood or think?
      (when (and @left @right) ; Are both of my forks available?
        (do
          (ref-set left false)
          (ref-set right false)
          (Thread/sleep 100)
          ;(handle-forks id :take)
          (alter rounds dec)
          (send-off logger debug id "ate      " @rounds)
          ;(handle-forks id :release)
          (ref-set left true)
          (ref-set right true))
        )))
  (Thread/sleep 100)
  (send-off *agent* behave id left right)) ; Repeat above

(defn start []
  (do
    (send-off (nth philosophers 0) behave 0 fork1 fork2)
    (send-off (nth philosophers 1) behave 1 fork2 fork3)
    (send-off (nth philosophers 2) behave 2 fork3 fork4)
    (send-off (nth philosophers 3) behave 3 fork4 fork5)
    (send-off (nth philosophers 4) behave 4 fork5 fork1)))


(defn -main [& args]
  (start))