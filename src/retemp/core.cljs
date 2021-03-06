(ns ^:figwheel-always retemp.core
  (:require [reagent.core    :as     r]
            [cljs.core.async :refer [<! >! put! chan timeout]])

  (:require-macros [cljs.core.async.macros :refer [go alt! go-loop]]))

(enable-console-print!)

;; VIEW

(defn hidbutton
  [label]
  (let [isHidden (r/atom false)]
    (fn []
      (when-not @isHidden
        [:button.btn.btn-warning
         {:on-click #(reset! isHidden true) :type "button"} label]))))

(def ^:private counter (r/atom 0))

(defn main-view []
  [:div.container
   [:h2 (str "Kliknąłeś " @counter " raz(y).")]
   [:button.btn.btn-success {:on-click #(swap!  counter inc) :type "button"} "Add One"]
   [:button.btn.btn-danger  {:on-click #(reset! counter   0) :type "button"} "Zero"]
   [hidbutton "Hide me !!!"]
   [hidbutton "Hide me too !!!"]])

(println (list   'a 'b 'c 'd))   ;; Lista jednok.
(println (hash-map "a" 1 "b" 2)) ;; Mapa

(println (vector 'a 'b 'c 'd)) ;; Wektor


;; INSTRUMENTATION

(defn on-js-reload
  []
  (println "Reloaded...")
  (r/render-component [main-view] (. js/document (getElementById "app"))))

(defn init
  []
  (on-js-reload))

(defonce start
  (init))



;; ;; ASYNC
;;
#_ (let [c (chan)]
     (go
       (println "writer: start")
       (>! c 15)
       (println "writer: wrote 15")
       (>! c 16)
       (println "writer: wrote 16")
       (println "writer: end"))

     (go
       (println "reader: start")
       (println "reader: got" (<! c))
       (println "reader: got" (<! c))
       (println "reader: end")))

#_ (go
     (.log js/console "Hello")
     (<! (timeout 2000))
     (.log js/console "async")
     (<! (timeout 2000))
     (.log js/console "world!"))

#_ (let [c (chan)
         t (timeout 2000)]
     (go
       (alt!
         c (.log js/console "Odebrałem wartość z kanału c")
         t (.log js/console "Upłynął czas 2s")))

     (go
       (<! (timeout 1000))
       (>! c 23)
       ))

;; (let [c (chan)
;;       t (timeout 2000)]
;;   (go
;;     (alt!
;;       c ([v] (.log js/console (str "Odebrałem wartość " v " z kanału c")))
;;       t ([v] (.log js/console (str "Upłynął czas 2s po których odebrałem " v)))))

;;   (go (<! (timeout 1000)) (>! c 23))
;;   (go (<! (timeout  900)) (>! c 24))
;;   (go (<! (timeout  200)) (>! c 25))
;;   (go (<! (timeout  356)) (>! c 26))

;;   (go (<! (timeout  300)) (>! c 23)
;;       (<! (timeout  900)) (>! c 24)
;;       (<! (timeout  200)) (>! c 25)
;;       (<! (timeout  356)) (>! c 26)))

;; #_ (let [c (chan)]
;;   (go-loop []
;;     (.log js/console (str "Odczytałem " (<! c)))
;;     (recur))

;;   (go (<! (timeout 1000)) (>! c 23)
;;       (<! (timeout  900)) (>! c 24)
;;       (<! (timeout  200)) (>! c 25)
;;       (<! (timeout  356)) (>! c 26)))

;; #_ (defn events-chan
;;   [element event-type]
;;   (let [c (chan 100)]
;;     (.addEventListener element event-type (fn [e] (put! c e)))
;;     c))

;; #_ (let [mouse-c (events-chan js/window "mousemove")]
;;   (go
;;     (while true
;;       (.log js/console (<! mouse-c)))))

;; ;; VIEW
