(ns status-im.contacts.validations
  (:require [cljs.spec :as s]
            [cljsjs.web3]
            [status-im.persistence.realm.core :as realm]))

(defn is-address? [s]
  (.isAddress js/Web3.prototype s))

(defn unique-identity? [identity]
  (not (realm/exists? :account :contact {:whisper-identity identity})))

(defn valid-length? [identity]
  (let [length (count identity)]
    (or
      (= 130 length)
      (= 132 length)
      (is-address? identity))))

(s/def ::identity-length valid-length?)
(s/def ::unique-identity unique-identity?)
(s/def ::not-empty-string (s/and string? not-empty))
(s/def ::name ::not-empty-string)
(s/def ::whisper-identity (s/and ::not-empty-string
                                 ::unique-identity
                                 ::identity-length))

(s/def ::contact (s/keys :req-un [::name ::whisper-identity]
                         :opt-un [::phone ::photo-path ::address]))