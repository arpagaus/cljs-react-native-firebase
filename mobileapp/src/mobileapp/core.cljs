(ns mobileapp.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [mobileapp.handlers]
            [mobileapp.subs]
            [goog.string :as gstring]
            [goog.string.format]
            [cljs-time.format :as tformat]))

(def ReactNative (js/require "react-native"))
(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))
(def Alert (.-Alert ReactNative))

(def Expo (js/require "expo"))
(def Constants (r/adapt-react-class (.-Constants Expo)))
(def Svg (r/adapt-react-class (.-Svg Expo)))
(def Rect (r/adapt-react-class (.-Rect (.-Svg Expo))))
(def Circle (r/adapt-react-class (.-Circle (.-Svg Expo))))
(def SvgText (r/adapt-react-class (.-Text (.-Svg Expo))))
(def TSpan (r/adapt-react-class (.-TSpan (.-Svg Expo))))
(def G (r/adapt-react-class (.-G (.-Svg Expo))))

(def NativeBase (js/require "native-base"))
(def Container (r/adapt-react-class (.-Container NativeBase)))
(def Header (r/adapt-react-class (.-Header NativeBase)))
(def Title (r/adapt-react-class (.-Title NativeBase)))
(def Content (r/adapt-react-class (.-Content NativeBase)))
(def Footer (r/adapt-react-class (.-Footer NativeBase)))
(def FooterTab (r/adapt-react-class (.-FooterTab NativeBase)))
(def Button (r/adapt-react-class (.-Button NativeBase)))
(def Left (r/adapt-react-class (.-Left NativeBase)))
(def Right (r/adapt-react-class (.-Right NativeBase)))
(def Body (r/adapt-react-class (.-Body NativeBase)))
(def Icon (r/adapt-react-class (.-Icon NativeBase)))
(def Text (r/adapt-react-class (.-Text NativeBase)))
(def Badge (r/adapt-react-class (.-Badge NativeBase)))
(def Card (r/adapt-react-class (.-Card NativeBase)))
(def CardItem (r/adapt-react-class (.-CardItem NativeBase)))
(def View (r/adapt-react-class (.-View NativeBase)))
(def Fab (r/adapt-react-class (.-Fab NativeBase)))
(def NBList (r/adapt-react-class (.-List NativeBase)))
(def ListItem (r/adapt-react-class (.-ListItem NativeBase)))
(def Separator (r/adapt-react-class (.-Separator NativeBase)))

(def VectorIcons (js/require "react-native-vector-icons"))
(def MaterialIcons (r/adapt-react-class (.-MaterialIcons VectorIcons)))

(def Firebase (js/require "firebase"))

(defonce firebase-app
  (.initializeApp Firebase  (clj->js {:apiKey ""
                                      :authDomain "purchase-analyzer.firebaseapp.com"
                                      :databaseURL "https://purchase-analyzer.firebaseio.com"
                                      :storageBucket "purchase-analyzer.appspot.com"
                                      :projectId "purchase-analyzer"})))

(def database (.database Firebase))
(def purchase-reference (.ref database "users/remo/purchases"))

(.set purchase-reference (clj->js (:purchases mobileapp.db/app-db)))

(.off purchase-reference)
#_(.on purchase-reference "value" #(cljs.pprint/pprint (js->clj (.val %) :keywordize-keys true)))
#_(.on purchase-reference "value" #(dispatch [:set-purchases (js->clj (.val %) :keywordize-keys true)]))

(defn alert [title]
  (.alert Alert title))

(def date-time-formatter (tformat/formatter "dd. MMM HH:mm"))

(def native-base-playground
  [Content {:padder true}
   [Text "Hi there! This is my first react native app"]
   [Badge
    [Text "2343"]]
   [Button {:dark true}
    [Text "Button"]]
   [Button {:block true}
    [Text "Block Button"]]
   [Card
    [CardItem
     [Body
      [Text "Also provides default spacing and alignment between cards..."]]]
    [CardItem
     [Text "Next card item. Still there?"]]
    [CardItem
     [Button [Text "OK"]]
     [Button [Text "Cancel"]]]]
   [Svg {:height 200 :width 200}
      ;[Rect {:x 50 :y 0 :height 50 :width 50 :fill "green"}]
    [Circle {:cx 100 :cy 100 :r 50 :fill "green"}]]])

(def radius (/ 100.0 (* 2 Math/PI)))

(def donut-segment-base
  {:cx 20 :cy 20 :r radius :fill "transparent" :strokeWidth 5 :onPress #(println "pressed donut!")})

(def segment-gap (/ 1 4))

(defn donut-segments
  [values]
  (let [factor (/ 100 (reduce + (map first values)))
        relative-values (->> values (sort-by #(nth % 0)) reverse (map (fn [[val color]] [(* factor val) color])))
        segments (map (fn [[val color]] (merge donut-segment-base {:stroke color :strokeDasharray [(- val segment-gap) (- 100 val (- segment-gap))]})) relative-values)]
    (loop [sum (- (first (first relative-values)) segment-gap)
           result [(first segments)]
           segments (next segments)]
      (if segments
        (let [segment (first segments)
              {[val _] :strokeDasharray} segment]
          (recur
           (+ sum val segment-gap)
           (conj result (assoc segment :strokeDashoffset (- 100 sum segment-gap)))
           (next segments)))
        result))))

(defn donut-chart  [values]
  (let [group [G {:rotate -90 :originX 20 :originY 20}]
        segments (mapv (fn [x] [Circle x]) (donut-segments values))
        donut (into [] (concat group segments))]
    [Svg {:height 300 :width 300 :viewBox "0 0 40 40"}
     (into [] (concat group segments))
     [SvgText {:x 20 :y 14 :textAnchor "middle" :fontSize 2 :fontWeight "bold"} "Total"]
     [SvgText {:x 20 :y 20 :textAnchor "middle" :fontWeight "bold" :onPress #(println "pressed text!")}
      [TSpan {:fontSize 2} "CHF"]
      [TSpan {:dy -5  :fontSize 4} "119.75"]]]))

(defn circle-icon [color]
  [Svg {:height 30 :width 30 :viewBox "0 0 2.2 2.2"} [Circle {:cx 1.1 :cy 1.1 :r 1 :fill color  :strokeWidth "0.08"}]])

(defn chart-detail
  [{:keys [id text value color icon], :as item}]
  [CardItem {:button true :onPress #(println "pressed button:" id)}
   [Left (circle-icon color) [Text text]]
   [Right [Text (gstring/format "CHF %.2f" value)]]])

(defn chart-details
  [items]
  (->>
   items
   (sort-by :value)
   reverse
   (map chart-detail)
   (cons Card)
   (into [])))

(defn summary-screen []
  (let [greeting (subscribe [:get-greeting])
        category-aggregates (subscribe [:get-category-aggregates])]
    (fn []
      [Container {:paddingTop 24}
       [Content {:padder true :paddingBottom 20}
        [View {:style {:flexDirection "row" :justifyContent "center"}}
         [Button {:bordered true :iconLeft true} [Icon {:name "calendar"}] [Text "Letze 7 Tage"]]]
        [View {:style {:flexDirection "row" :justifyContent "center"}}
         (donut-chart (map (fn [{v :value c :color}] [v c]) @category-aggregates))]
        (chart-details @category-aggregates)]])))

(defn purchase-list-item
   [row-js]
   (let [{:keys [header text turnover] :as row} (js->clj row-js :keywordize-keys true)]
     (r/as-element
       [ListItem {:itemDivider header}
        [Left
         [Text text]]
        [Right
         [Text (gstring/format "CHF %.2f" turnover)]]])))

(defn ->purchase-list-data
  [purchases]
  (->>
    purchases
    (map (fn [{:keys [location instant] :as purchase}]
           (let [text (str #_(tformat/unparse date-time-formatter instant) ", " location)
                 purchase-enriched (merge purchase {:header true :text text})]
             (cons purchase-enriched (:articles purchase)))))
    flatten))

(defn app-root []
  (let [purchases (subscribe [:get-purchases])]
    (fn []
      [Container {:paddingTop 24}
       [Header
        [Left
         [Button {:transparent true}
          [Icon {:name "menu"}]]]
        [Body
         [Title "Einkäufe"]]
        [Right]]
       [Content
        [NBList {:dataArray (->purchase-list-data @purchases)
                 :renderRow purchase-list-item}]]])))


(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "main" #(r/reactify-component app-root)))
