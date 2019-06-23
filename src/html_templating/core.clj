(ns html-templating.core
  (:require [selmer.parser :as selmer]
            [selmer.filters :as filters]))

;; (let [template "{% if files|empty? %}no files{% else %}files{% endif %}"]
;;     (selmer/render template {:files []}))
;; => "no files"
(filters/add-filter! :empty? empty?)

;; By default, the ouput is escaped:
;;
;; (let [template {:input "<div>I'm not safe</div>"}]
;;     (selmer/render "{{input|upper}}" template))
;; => "&lt;DIV&gt;I&#39;M NOT SAFE&lt;/DIV&gt;"
;;
;; If we know the input is safe we can prevent escaping in our filter:
;;
;; (let [template {:input "<div>I'm safe</div>"}]
;;     (selmer/render "{{input|unescaped-upper}}" template))
;; => "<DIV>I'M SAFE</DIV>"
(filters/add-filter! :unescaped-upper
                     (fn [x] [:safe (.toUpperCase x)]))

;; Weirdly this does not work, I'm not sure why because the :empy?
;; filter is defined the same way. But I get:
;; "unable to resolved symbol: .toUpperCse in this context"
;; (filters/add-filter! :foo .toUpperCase)



;; (let [template "{% image \"http://foo.com/logo.png\" %}"]
;;   (selmer/render template {}))
;; => "<img src=\"http://foo.com/logo.png\" />"
(selmer/add-tag!
 :image
 (fn [args context-map]
   (str "<img src=" (first args) "/>")))

;; (let [template "{% image-with-src %}"]
;;   (selmer/render template {:src "http://foo.com/logo.png"}))
;; => "<img src=\"http://foo.com/logo.png\" />"
(selmer/add-tag!
 :image-with-src
 (fn [args context-map]
   (str "<img src=\"" (:src context-map) "\" />")))

;; (selmer/render "{% uppercase %}foo {{bar}} baz{% enduppercase %}"
;;                {:bar "injected"})
;; => "FOO INJECTED BAZ"
(selmer/add-tag!
 :uppercase
 (fn [args context-map content]
   (.toUpperCase (get-in content [:uppercase :content])))
 :enduppercase)
