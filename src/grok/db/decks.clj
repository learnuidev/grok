(ns grok.db.decks
  (:require [datomic.api :as d]
            [grok.db.core :refer [conn]]))

(defn browse
  "Browse a list of decks, blonging to a certain user"
  [db user-id]
  (d/q '[:find [(pull ?deck [*]) ...]
         :in $ ?uid
         :where
         [?user :user/id ?uid]
         [?deck :deck/author ?user]]
       db user-id))
; - Read
(defn fetch
  "Fetch a single deck by ID"
  [db user-id deck-id])

; - Create
(defn create!
  "Create a new deck"
  [conn user-id deck-params])

; - Update
(defn edit!
  "Edit an existing deck"
  [conn user-id deck-id deck-params])

; - Delete
(defn delete!
  "Delete a deck"
  [conn user-id deck-id])
