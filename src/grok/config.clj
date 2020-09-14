(ns grok.config
  (:require [mount.core :refer [defstate]]
            [config.core :as config]))

;; Lets define our first state
(defstate env
          :start config/env)
