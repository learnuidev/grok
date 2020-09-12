(ns grok.db.core
  (:require [datomic.api :as d]
            [grok.db.schema :refer [schema]]))

;; TODO
;; - 1. create a db-uri
(def database-uri "datomic:sql://grok-development?jdbc:postgresql://localhost:5432/datomic?user=datomic&password=datomic")

(defn create-conn [db-uri]
  (d/create-database db-uri)
  (let [conn (d/connect db-uri)]
    conn))

;; - 2. create a connection
(def conn (create-conn database-uri))

;; - 3. create schema and transact into the database
(comment
  (def tx @(d/transact conn schema)))
