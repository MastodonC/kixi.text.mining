(defproject kixi.text.mining "0.1.0-SNAPSHOT"
  :description "MastodonC's Kixi Text Mining Platform"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/data.csv "0.1.3"]
                 [org.apache.poi/poi "3.11"]
                 [org.apache.poi/poi-scratchpad "3.11"]
                 [org.apache.poi/poi-ooxml "3.11"]
                 [org.clojure/data.csv "0.1.3"]
                 [org.clojure/tools.cli "0.3.3"]
                 [marcliberatore.mallet-lda "0.1.1"]
                 [cc.mallet/mallet "2.0.7"]
                 [snowball-stemmer "0.1.0"]]
  :resource-paths ["resources"]
  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :main kixi.text.mining
  :target-path "target/%s"
  :plugins [[lein-environ "1.0.0"]
            [lein-cljfmt "0.1.10"]
            [jonase/eastwood "0.2.1"]]
  :profiles {:dev
             {:dependencies [[criterium "0.4.3"]]}}
  :jvm-opts ["-Duser.timezone=UTC"
             "-XX:MaxPermSize=512m"
             "-Xmx4G"
             "-XX:+CMSClassUnloadingEnabled"
             "-XX:+UseCompressedOops"
             "-XX:+HeapDumpOnOutOfMemoryError"]
  )
