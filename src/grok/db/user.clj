(ns grok.db.user
  (:require [datomic.api :as d]
            [grok.db.core :refer [conn]]
            [clojure.spec.alpha :as s]
            [clojure.test.check.generators :as gen]))

(defn validate-email [email]
  (let [email-regex #"^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$"]
    (re-matches email-regex email)))

(s/def :user/email
  (s/with-gen
   (s/and string? validate-email)
   #(s/gen #{"john@gmail.com" "jane@me.com"})))
(s/def :user/password
  (s/with-gen
   (s/and string? #(> (count %) 6))
   #(s/gen #{"abcdeffas" "asdasdasd231da" "asdas3wa2ed"})))

(s/def :user/username
  (s/with-gen
   string?
   #(s/gen #{"john.doe" "mark.dijk" "mo.salah"})))

(s/def :user/token
  (s/with-gen
   string?
   #(s/gen #{"abcdeff" "asdasdasd231da" "asdas3wa2ed"})))

(s/def :user/id uuid?)

(s/def ::user
  (s/keys :req [:user/email :user/password]
          :opt [:user/id :user/token :user/username]))

(defn create! [conn user-params]
  (if (s/valid? ::user user-params)
    (let [user-id (d/squuid)
          tx-data (merge user-params {:user/id user-id})]
      (d/transact conn [tx-data])
      user-id)
    (throw (ex-info "User is invalid"
                    {:grok/error-id :validation
                     :error "Invalid email or password provided"}))))

(defn fetch
  ([db user-id]
   (fetch db user-id '[*]))
  ([db user-id pattern]
   (d/q '[:find (pull ?uid pattern) .
          :in $ ?user-id pattern
          :where
          [?uid :user/id ?user-id]]
        db user-id pattern)))

(defn edit!
  [conn user-id user-params]
  (if (fetch (d/db conn) user-id)
    (let [tx-data (merge user-params {:user/id user-id})
          db-after (:db-after @(d/transact conn [tx-data]))]
      (fetch db-after user-id))
    (throw (ex-info "Unable to update user"
                    {:grok/error-id :server-error
                     :error "Unable to edit user"}))))

(defn delete!
  [conn user-id]
  (when-let [user (fetch (d/db conn) user-id)]
    (d/transact conn [[:db/retractEntity [:user/id user-id]]])
    user))
