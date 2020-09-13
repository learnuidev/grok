(ns grok.db.user
  (:require [datomic.api :as d]
            [clojure.string :as str]
            [clojure.spec.alpha :as s]))

;; id
;; email
;; username
;; password
;; token

(defn validate-email [email]
  (let [email-regex #"^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$"]
    (re-matches email-regex email)))

(validate-email "joe@gmail.com")

(s/def :user/email (s/and string? validate-email))
(s/def :user/password (s/and string? #(> (count %) 6)))
(s/def :user/username string?)
(s/def :user/token string?)
(s/def :user/id string?)

(count "sadasd")

(s/def ::user
  (s/keys :req [:user/email :user/password]
          :opt [:user/id :user/token :user/username]))

(defn create! [conn user-params]
  (if (s/valid? ::user user-params)
    (let [user-id (d/squuid)
          tx-data (merge {:user/id user-id} user-params)]
      (d/transact conn [tx-data])
      user-id)
    (throw (ex-info "User is invalid"
                    {:grok/error-id :validation
                     :error "Invalid email or password provided"}))))
