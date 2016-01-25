(ns kixi.text.mallet.model
  (:require [marcliberatore.mallet-lda :as lda]
            [marcliberatore.mallet-lda.misc :as lda-misc]
            [kixi.text.wordfunctions :as wordf]
            [clojure.data.csv :as csv]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(defn getcsv [filepath]
  (with-open [csv-file (io/reader filepath)]
    (doall
     (csv/read-csv csv-file))))

(defn load-stopwords [stopwords-filepath]
  (-> stopwords-filepath
      (slurp)
      (str/split #"[\r\n]+")))


(defn make-lda-with-vector [labeled-vector topics]
  (lda/lda
   (->> labeled-vector
        (keep (fn [[label word-vector]]
                (when (seq word-vector)
                  (vector label word-vector))))
        (lda/make-instance-list))
   :num-topics topics
   :num-iter 1000))
