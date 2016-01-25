(ns kixi.text.analysis
  (:require [kixi.text.docscrubber :as docscrub]
            [kixi.text.mallet.model :as model]
            [clojure.java.io :as io]))

(defn write-file [path filename]
  (io/as-file (str path filename)))

(defn run-mining [dir topics ngrams stopwords-file output-model-filepath]
  (let [path (str output-model-filepath (System/getProperty "file.separator"))
        topicmodel (-> dir
                       (docscrub/process-docs-to-vectors ngrams stopwords-file)
                       (model/make-lda-with-vector topics))]
    (-> topicmodel (.write (write-file path "topicmodel.model")))
    (-> topicmodel (.printDocumentTopics (write-file path "documenttopics.txt")))
    (-> topicmodel (.printTypeTopicCounts (write-file path "doctypetopiccounts.txt")))
    (-> topicmodel (.printTopicWordWeights (write-file path "topicwordweights.txt")))))
