(ns try-lacinia.streamer)

(def *ping-subscribes (atom 0))
(def *ping-cleanups (atom 0))

(defn stream-ping
  [context {:keys [message count]} source-stream]
  (swap! *ping-subscribes inc)
  (let [runnable ^Runnable (fn []
                             (dotimes [i count]
                               (Thread/sleep 500)
                               (source-stream {:message   (str message " #" (inc i))
                                               :timestamp (System/currentTimeMillis)}))

                             (source-stream nil))]
    (.start (Thread. runnable "stream-ping-thread")))
  ;; Return a cleanup fn:
  #(swap! *ping-cleanups inc))