(ns grok.db.core
  (:require [datomic.api :as d]
            [config.core :refer [env]]
            [grok.db.schema :refer [schema]]))

(defn create-conn [db-uri]
  (when db-uri
    (d/create-database db-uri)
    (let [conn (d/connect db-uri)]
      conn)))

;; Conn
(def conn (create-conn (:database-uri env)))

;; Schema transaction
(comment
  (def tx @(d/transact conn schema)))
