(ns kixi.text.mallet.predict
  (:require [marcliberatore.mallet-lda :as lda]
            [marcliberatore.mallet-lda.misc :as lda-misc]
            [clojure.java.io :as io]))

(defn get-probability [model document]
  (let [inferencer (.getInferencer model)]
    (.getSampledDistribution inferencer document 10 1 5)))

(defn load-model [modelname]
  (-> modelname
      (io/as-file)
      (cc.mallet.topics.ParallelTopicModel/read)))

(defn get-instance [filename]
  (->> filename
       (io/as-file)
       (vector)
       (map #(vector (.getName %)
                     (-> %
                         (slurp)
                         (clojure.string/split #" "))))
       (lda/make-instance-list)
       (first)))

(defn make-prediction [modelname testdocument]
  (let [model (load-model modelname)
        probabilities (-> testdocument
                          (get-instance)
                          ((partial get-probability model))
                          (seq))

        toptopics (->> (.getTopWords model 10)
                       (seq)
                       (mapv #(seq %)))]
    (->> (mapv #(vector %1 %2) probabilities toptopics)
         (sort-by #(first %))
         (reverse))))

(comment
  (def zip (->>  (mapv  #(vector %1 %2) (seq values) (mapv #(seq %) (seq toptopics))) (sort-by #(first %)))))
