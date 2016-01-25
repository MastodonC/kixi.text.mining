# kixi.text.mining

A project to extract topics from text files using the [Latent Dirichlet Allocation](https://en.wikipedia.org/wiki/Latent_Dirichlet_allocation) (LDA) model.

The LDA model is generated by using a Clojure library ([mallet-lda](https://github.com/marcliberatore/mallet-lda)) that is a wrapper for the [Mallet](http://mallet.cs.umass.edu/) toolkit.

## Usage

To run the programme use the bash script `run-text-mining.sh`:
```
~$ ./run-text-mining.sh
```

The parameters are set directly inside the script:
```
VERSION=0.1.0-SNAPSHOT
INPUTDIR=/user/input/
TOPICS=20
NGRAMS=2
STOPWORDFILE=/user/stopwords.txt
OUTPUTDIR=/user/output/
```

They should be edited so that:
* `VERSION` corresponds to the right version of the jar file
* `INPUTDIR` corresponds to the input directory containing the text files
* `TOPICS` corresponds to the number of topics wanted (defaults to 20)
* `NGRAMS` corresponds to the number of words in the ngram (defaults to 2). For single word sequences add `1`.
* `STOPWORDFILE` corresponds to the path to the stopwords file
* `OUTPUTFILE` corresponds to the directory where the model will be outputed.


## License

Copyright © MastodonC 2015

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
