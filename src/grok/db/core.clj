(ns grok.db.core
  (:require [datomic.api :as d]
            [grok.config :refer [env]]
            [mount.core :as mount :refer [defstate]]
            [grok.db.schema :refer [schema]]))

(defn create-conn [db-uri]
  (when db-uri
    (d/create-database db-uri)
    (let [conn (d/connect db-uri)]
      conn)))

;; Lets change our conn to mount state
(defstate conn
          :start (create-conn (:database-uri env))
          :stop  (.release conn))

;; Schema transaction
(comment
  (def tx @(d/transact conn schema)))
