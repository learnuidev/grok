(ns grok.db.with-db
  (:require [grok.db.core :as SUT]
            [grok.db.users :as u]
            [grok.db.schema :refer [schema]]
            [clojure.spec.alpha :as s]
            [clojure.test.check.generators :as gen]
            [datomic.api :as d]))

(def ^:dynamic *conn* nil)

(def user-params)
(def sample-user
  (-> (gen/generate (s/gen ::u/user))
      (merge {:user/email "test@test.com"
              :user/id (d/squuid)})))

(defn fresh-db []
  (let [db-uri (str "datomic:mem://" (gensym))
        conn (SUT/create-conn db-uri)]

    (d/transact conn schema)
    (d/transact conn [sample-user])
    conn))

(defn with-db [f]
  (binding [*conn* (fresh-db)]
    (f)))
