(ns html-templating.core
  (:require [selmer.parser :as selmer]
            [selmer.filters :as filters]))

;; (let [template "{% if files|empty? %}no files{% else %}files{% endif %}"]
;;     (selmer/render template {:files []}))
;; => "no files"
(filters/add-filter! :empty? empty?)
