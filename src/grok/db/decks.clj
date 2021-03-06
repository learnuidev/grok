(ns grok.db.decks
  (:require
   [datomic.api :as d]
   [clojure.spec.alpha :as s]
   [clojure.test.check.generators :as gen]
   [grok.db.core :refer [conn]]))

;; Deck Spec
(s/def :deck/id uuid?)
(s/def :deck/title (s/and string? #(seq %)))
(s/def :deck/tags  (s/coll-of string? :kind vector? :min-count 1))

(s/def ::deck
  (s/keys
   :req [:deck/title :deck/tags]
   :opt [:deck/id]))

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
;; small typo
(defn fetch
  "Fetch a single deck by ID, returns nil if not found"
  [db user-id deck-id]
  (d/q '[:find (pull ?deck [*]) .
         :in $ ?uid ?did
         :where
         [?user :user/id ?uid]
         [?deck :deck/id ?did]
         [?deck :deck/author ?user]]
       db user-id deck-id))

; - Create
(defn create!
  "Create a new deck"
  [conn user-id deck-params]
  (if (s/valid? ::deck deck-params)
    (let [deck-id (d/squuid)
          tx-data (merge deck-params {:deck/author [:user/id user-id]
                                      :deck/id deck-id})]
      (d/transact conn [tx-data])
      deck-id)
    (throw (ex-info "Deck is invalid"
                    {:grok/error-id :validation
                     :error "Invalid deck input values"}))))
; - Update
(defn edit!
  "Edit an existing deck"
  [conn user-id deck-id deck-params]
  (if (fetch (d/db conn) user-id deck-id)
    (let [tx-data (merge deck-params {:deck/id deck-id})
          db-after (:db-after @(d/transact conn [tx-data]))]
      (fetch db-after user-id deck-id))
    (throw (ex-info "Unable to update deck"
                    {:grok/error-id :server-error
                     :error "Unable to update deck"}))))

; - Delete
(defn delete!
  "Delete a deck"
  [conn user-id deck-id]
  (when-let [deck (fetch (d/db conn) user-id deck-id)]
    (d/transact conn [[:db/retractEntity [:deck/id deck-id]]])
    deck))
