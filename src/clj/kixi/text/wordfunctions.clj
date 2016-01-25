(ns kixi.text.wordfunctions
  (:require [clojure.string :as str]))

(defn word-length-valid [word min max]
  (cond (>= (count word) min) (<= (count word) max)
        :else false))

(defn min-paragraph-length-valid [para minwords]
  ;;
  (> (count para) minwords))

(defn parse-paragraph [word-vector min-word-length max-word-length]
  ;; Removes words that are less than the min word length and more than the max word length.
  (->> word-vector
       (keep (fn [word]
               (when (word-length-valid word min-word-length max-word-length) (.trim word))))))


(defn remove-numbers [text-in]
  (.trim (str/replace text-in #"\d+" "")))

(defn remove-punctuation [text-in]
  (-> text-in
      (str/replace #"[\.,\?:;!\"'†+\/\(\)\\]+$" "") ;; end of the line
      (str/replace #"^[\.,\?:;!\"'†+\/\(\)\\]+" "") ;; beginning of the line
      (str/replace #"(\S)[,\?:;!\"'†+\(\)\\]+(\S)" "$1 $2") ;; with non-space chars either side
      (str/replace #"(\S)[\.,\?:;!\"'†+\/\(\)\\]+\s" "$1 ") ;; non-space before and space after
      (str/replace #"\s[\.,\?:;!\"'†+\/\(\)\\]+(\S)" " $1") ;; space before and non-space after
      (str/replace #"\s[-\.,\?:;!\"'†+\/\(\)\\]+\s" " ") ;; space either side
      (str/replace #" – " " ") ;; pesky em-dashes
      (str/replace #"[%\/\\\|#.&\"-*\[\]\{\}£=]+" " ")))

(defn remove-stopwords [words stop-words]
  (remove (set stop-words) words))
