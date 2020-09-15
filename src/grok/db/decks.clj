(ns grok.db.decks
  (:require [datomic.api :as d]
            [grok.db.core :refer [conn]]))

(defn browse
  "Browse a list of decks, blonging to a certain user, returns
   empty vector if user has not created any deck"
  [db user-id]
  (d/q '[:find [(pull ?deck [*]) ...]
         :in $ ?uid
         :where
         [?user :user/id ?uid]
         [?deck :deck/author ?user]]
       db user-id))

; - Read
;; passing . after find caluse returns a single item
(defn fetch
  "Fetch a single deck by ID, returns nil if not found"
  [db user-id deck-id]
  (d/q '[:find (pull ?deck [*]) .
         :in $ ?uid ?did
         :where
         [?user :user/id ?uid]
         [?deck :deck/id ?did]
         [?deck :deck/author ?author]]
       db user-id deck-id))

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
