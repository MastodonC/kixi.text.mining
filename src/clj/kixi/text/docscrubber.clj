(ns kixi.text.docscrubber
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.data.csv :as csv]
            [kixi.text.wordfunctions :as wordf]
            [stemmer.snowball :as snowball]))

(def stemmer (snowball/stemmer :english))

(defn n-grams [n words]
  (->> (partition n 1 words)
       (map (partial str/join " "))))

(defn remove-punctuation-in-string [s]
  (-> s
      (str/replace #"[\.,\?:;!\"'†+\/\(\)\\]+$" "") ;; end of the line
      (str/replace #"^[\.,\?:;!\"'†+\/\(\)\\]+" "") ;; beginning of the line
      (str/replace #"(\S)[,\?:;!\"'†+\(\)\\]+(\S)" "$1 $2") ;; with non-space chars either side
      (str/replace #"(\S)[\.,\?:;!\"'†+\/\(\)\\]+\s" "$1 ") ;; non-space before and space after
      (str/replace #"\s[\.,\?:;!\"'†+\/\(\)\\]+(\S)" " $1") ;; space before and non-space after
      (str/replace #"\s[-\.,\?:;!\"'†+\/\(\)\\]+\s" " ") ;; space either side
      (str/replace #" – " " ") ;; pesky em-dashes
      (str/replace #"\d+" "")))

(defn scrub-doc [filename paragraphs]
  (->> paragraphs
       (map str/lower-case)
       (map remove-punctuation-in-string)
       (map #(str/split % #" "))
       (map #(mapv stemmer %))
       (map #(str/join " " %))
       (map (fn [n strings] (vector filename n strings)) (range))))

(defn docx->words [f]
  (with-open [in (io/input-stream f)]
    (let [worddoc (new org.apache.poi.xwpf.usermodel.XWPFDocument
                       (org.apache.poi.openxml4j.opc.OPCPackage/open in))
          extractor (new org.apache.poi.xwpf.extractor.XWPFWordExtractor worddoc)]
      (vector (.getText extractor)))))

(defn docx->paragraphs [f]
  (with-open [in (io/input-stream f)]
    (let [worddoc (new org.apache.poi.xwpf.usermodel.XWPFDocument
                       (org.apache.poi.openxml4j.opc.OPCPackage/open in))
          paragraphs (.getParagraphs worddoc)]
      (mapv (fn [para] (.getText para)) paragraphs))))

(defn save-csv [filename data]
  (with-open [out-file (io/writer filename)]
    (csv/write-csv out-file data)))

(defn process-file-paragraphs [fileobj]
  (println (str "Working on : " (.getAbsolutePath fileobj)))
  (->> fileobj
       docx->paragraphs
       (scrub-doc (.getName fileobj))))

(defn process-file-fulltext [fileobj]
  (->> fileobj
       (docx->words)
       (scrub-doc (.getName fileobj))))

(defn load-stopwords [stopwords-filepath]
  (-> stopwords-filepath
      (slurp)
      (str/split #"[\r\n]+")))

(defn clean-string-input
  "Cleans strings for create-cleaned-ngrams-vector function."
  [str-input stopwords-file]
  (let [stopwords (load-stopwords stopwords-file)
        vec-words (-> str-input
                      (.trim)
                      (wordf/remove-numbers)
                      (wordf/remove-punctuation)
                      (str/split #"[\n ]+"))]
    (wordf/parse-paragraph
     (->> (wordf/remove-stopwords vec-words stopwords)
          (map #(.trim %))) ;; remove extra whitespaces like \n attached to a word
     4 20)))

(defn create-cleaned-ngrams-vector
  "Takes in a string and outputs a collection of
  cleaned words or n-grams."
  ([para stopwords-file]
   (clean-string-input para stopwords-file))
  ([n para stopwords-file]
   (n-grams n (clean-string-input para stopwords-file))))

(defn process-docs-to-vectors [root-directory-name n stopwords-file]
  ;; first check the directory exists
  (when (.exists (io/as-file root-directory-name))
    (->> root-directory-name
         (io/as-file)
         (file-seq)
         (filter #(re-matches #"^.*\.docx$" (.getName %)))
         (mapcat process-file-paragraphs)
         (keep (fn [[k1 k2 d]]
                 (vector (str k1 ":" k2)
                         (create-cleaned-ngrams-vector n d stopwords-file)))))))

(defn process-all-docs [root-directory-name n output-root stopwords-file]
  ;; first check the directory exists
  (let [files (when (.exists (io/as-file root-directory-name))
                (->> root-directory-name
                     (io/as-file)
                     (file-seq)
                     (filter #(re-matches #"^.*\.docx$" (.getName %)))))]
    (->> files
         (mapcat process-file-paragraphs)
         (map (fn [[k1 k2 d]]
                (vector (str k1 ":" k2 ", "
                             (create-cleaned-ngrams-vector n d stopwords-file)))))
         (vec)
         (save-csv (str output-root "_paras.csv")))
    (->> files
         (mapcat process-file-fulltext)
         (map (fn [[k1 k2 d]]
                (vector (str k1 ":" k2) d)))
         (vec)
         (save-csv (str output-root "_fulltext.csv")))))

(comment
  ;; Example usage.
  (->> (process-all-docs  "/path/to/docx/files")
       (save-csv "/path/to/csv/output/myfile.csv")))
