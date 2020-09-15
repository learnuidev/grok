(ns grok.db.decks-test
  (:require [clojure.test :refer [is deftest testing use-fixtures]]
            [datomic.api :as d]
            [clojure.spec.alpha :as s]
            [clojure.test.check.generators :as gen]
            [grok.db.decks :as SUT]
            [grok.db.user :as u]
            [grok.db.with-db :refer [with-db *conn*]]))

(use-fixtures :each with-db)

(deftest decks
  (testing "browse - returns empty vector, user has not created any deck"
    (let [user-params (gen/generate (s/gen ::u/user))
          uid (u/create! *conn* user-params)
          decks (SUT/browse (d/db *conn*) uid)]
      (is (= true (vector? decks)))
      (is (= true (empty? decks)))))

  (testing "browse - returns vector of decks, if available"
    (let [user-params (gen/generate (s/gen ::u/user))
          uid (u/create! *conn* user-params)
          new-deck {:deck/id (d/squuid)
                    :deck/title "Learning Clojure"
                    :deck/tags #{"Clojure" "programming"}
                    :deck/author [:user/id uid]}]
      @(d/transact *conn* [new-deck])
      (let [decks (SUT/browse (d/db *conn*) uid)]
        (is (= true (vector? decks)))
        (is (= false (empty? decks)))))))
