#!/bin/bash

VERSION=0.1.0-SNAPSHOT
INPUTDIR=/user/input/
TOPICS=20
NGRAMS=2
STOPWORDFILE=/user/stopwords.txt
OUTPUTDIR=/user/output/

java -jar ktirio.text.mining-${VERSION}-standalone.jar -d${INPUTDIR?NOT DEFINED}  -t${TOPICS?NOT DEFINED} -n${NGRAMS?NOT DEFINED} -s${STOPWORDFILE?NOT DEFINED} -o${OUTPUTDIR?NOT DEFINED}
