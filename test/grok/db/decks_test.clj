(ns grok.db.decks-test
  (:require [clojure.test :refer [is deftest testing use-fixtures]]
            [datomic.api :as d]
            [clojure.spec.alpha :as s]
            [clojure.test.check.generators :as gen]
            [grok.db.decks :as SUT]
            [grok.db.with-db :refer [with-db *conn*]]))

(use-fixtures :each with-db)

(deftest decks
  (let [user-id (:user/id (d/entity (d/db *conn*) [:user/email "test@test.com"]))]
    (testing "browse - returns empty vector, user has not created any deck"
      (let [decks (SUT/browse (d/db *conn*) user-id)]
        (is (= true (vector? decks)))
        (is (= true (empty? decks)))))

    (testing "browse - returns vector of decks, if available"
      (let [new-deck (merge (gen/generate (s/gen ::SUT/deck))
                            {:deck/author [:user/id user-id]})]
        @(d/transact *conn* [new-deck])
        (let [decks (SUT/browse (d/db *conn*) user-id)]
          (is (= true (vector? decks)))
          (is (= false (empty? decks))))))

    (testing "fetch - returns a single deck by deck ID, belonging to a user"
      (let [deck-id (d/squuid)
            new-deck (merge (gen/generate (s/gen ::SUT/deck))
                            {:deck/id deck-id
                             :deck/author [:user/id user-id]})]
        @(d/transact *conn* [new-deck])
        (let [deck (SUT/fetch (d/db *conn*) user-id deck-id)]
          (is (= true (map? deck)))
          (is (= false (empty? deck))))))
    (testing "fetch - returns nil if not found"
      (let [deck-id (d/squuid)
            deck (SUT/fetch (d/db *conn*) user-id deck-id)]
        (is (= false (map? deck)))
        (is (= true (nil? deck)))))

    (testing "create! - create a new deck"
      (let [new-deck (gen/generate (s/gen ::SUT/deck))
            deck-id (SUT/create! *conn* user-id new-deck)]
        (is (uuid? deck-id))))
    (testing "edit! - edit an existing deck"
      (let [new-deck (gen/generate (s/gen ::SUT/deck))
            deck-id (SUT/create! *conn* user-id new-deck)
            deck-params {:deck/title "Learning Datomic"}
            edited-deck (SUT/edit! *conn* user-id deck-id deck-params)]
        (is (= (:deck/title deck-params) (:deck/title edited-deck)))))
    (testing "delete! - delete an existing deck"
      (let [deck-id (SUT/create! *conn* user-id (gen/generate (s/gen ::SUT/deck)))
            deck (SUT/delete! *conn* user-id deck-id)]
        (is (= true (s/valid? ::SUT/deck deck)))
        (is (= nil (SUT/fetch (d/db *conn*) user-id deck-id)))))))
