(ns grok.db.cards
  (:require
   [clojure.spec.alpha :as s]
   [datomic.api :as d]
   [clojure.test.check.generators :as gen]))

;; Step 1 - Transact cards schema - DONE
;; Step 2 - Create cards spec     - DONE
(s/def :card/id uuid?)
(s/def :card/front string?)
(s/def :card/back string?)
(s/def :card/progress  (s/and #(> % 0) int?))
(s/def :card/next-study-date inst?)

(s/def ::card
  (s/keys :req [:card/front :card/back]
          :opt [:card/id :card/progress :card/next-study-date]))

;; Lets try to generate random card using generate function
;; (gen/generate (s/gen ::card))

(defn browse
  "list all the cards belonging to a certain deck"
  [db deck-id]
  (d/q '[:find [(pull ?cards [*]) ...]
         :in $ ?deck-id
         :where
         [?deck :deck/id ?deck-id]
         [?cards :card/deck ?deck]]
       db deck-id))

; - Read - fetch a single card by id
(defn fetch
  "Fetch a single card by ID, return nil if not found"
  [db deck-id card-id]
  (d/q '[:find (pull ?card [*]) .
         :in $ ?deck-id ?card-id
         :where
         [?deck :deck/id ?deck-id]
         [?card :card/id ?card-id]
         [?card :card/deck ?deck]]
       db deck-id card-id))
; - Create - create a new card
(defn create!
  "Create a new card"
  [conn deck-id card-params]
  (if (s/valid? ::card card-params)
    (let [card-id (d/squuid)
          tx-data (-> card-params
                      (assoc :card/deck [:deck/id deck-id])
                      (assoc :card/id card-id))]
      (d/transact conn [tx-data])
      card-id)
    (throw (ex-info "Card is invalid"
                    {:grok/error-id :validation
                     :error "Invalid card input values"}))))
; - Update - update a card
; - Delete - delete a card
