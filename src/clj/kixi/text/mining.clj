(ns kixi.text.mining
  (:require [clojure.tools.cli :refer [parse-opts]]
            [kixi.text.analysis :as kta])
  (:gen-class :main true))

(def cli-options
  [["-d" "--dir DIRECTORY" "Directory of .docx documents."]
   ["-t" "--topics TOPICS" "Number of topics (default 20)"
    :default 20 :parse-fn #(Integer/parseInt %)]
   ["-n" "--ngrams NGRAMS" "Number of words by ngram (default 2)"
    :default 2 :parse-fn #(Integer/parseInt %)
    :validate [#(> % 0) "Must be greater than zero."]]
   ["-s" "--stopwordfile STOPWORDFILE" "Path and filename of stopwords file."]
   ["-o" "--outputdir OUTPUTDIR" "Path to output directory."]])

(defn -main [& args]
  (let [{:keys [dir topics ngrams stopwordfile outputdir] :as opts}
        (:options (parse-opts  args cli-options))]
    (kta/run-mining dir topics ngrams stopwordfile outputdir)))
