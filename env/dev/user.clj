(ns user
  (:require [mount.core :as mount]
            [clojure.tools.namespace.repl :as tn]
            [grok.core]))

(defn refresh-ns
  "Refresh/reloads all the namespace"
  []
  (tn/refresh-all))

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
  (refresh-ns)
  (start))

(comment
  (restart-dev))
