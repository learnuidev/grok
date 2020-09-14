(ns user
  (:require [mount.core :as mount]
            [grok.core]))

(defn start
  "Mount starts life cycle of runtime state"
  []
  (mount/start))

(defn stop
  "Mount stops life cycle of runtime state"
  []
  (mount/stop))

(defn restart-dev
  []
  (stop)
  (start))

(comment
  (restart-dev))
