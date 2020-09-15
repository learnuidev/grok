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
    :db/doc "Token of the User"}
   {:db/ident :deck/id
    :db/valueType :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity
    :db/doc "ID of the deck"}
   {:db/ident :deck/author
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "Author of the deck"}
   {:db/ident :deck/title
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "Title of the deck"}
   {:db/ident :deck/tags
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/many
    :db/doc "Tags of the deck"}])
