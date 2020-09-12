(ns grok.db.schema)

(def schema
  [{:db/ident :user/id
    :db/valueType :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity
    :db/doc "ID of the User"}
   {:db/ident :user/email
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity
    :db/doc "Email of the User"}
   {:db/ident :user/username
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "Username of the User"}
   {:db/ident :user/password
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "Hashed Password of the User"}
   {:db/ident :user/token
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "Token of the User"}])
