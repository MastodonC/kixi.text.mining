
@ECHO OFF

set VERSION=0.1.0-SNAPSHOT
set INPUTDIR=c:\path\to\word\documents\
set TOPICS=20
set NGRAMS=2
set STOPWORDFILE=c:\path\to\ngram_stopwords_windows.txt
set OUTPUTDIR=c:\path\to\output\directory\

java -cp .;..;ktirio.text.mining-0.1.0-SNAPSHOT-standalone.jar ktirio.text.mining -d%INPUTDIR% -t%TOPICS% -n%NGRAMS% -s%STOPWORDFILE% -o%OUTPUTDIR%
