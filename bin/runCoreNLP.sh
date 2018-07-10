#!/bin/bash

set -e #fail last

MY_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" > /dev/null && pwd )"

source "${MY_DIR}/config.sh"


# location of input file. must be absolute path
INPUT_FILE="${SEMAFOR_HOME}/data/${1}"

# where to write the output
OUTPUT_DIR="${2}"

LANG="${3}"

TEST_PARSED_FILE="${OUTPUT_DIR}/conll"
ALIGNMENT_FILE="${OUTPUT_DIR}/json"

echo "**********************************************************************"
echo "Running CoreNLP...."
#-props StanfordCoreNLP-arabic.properties
pushd ${SEMAFOR_HOME}/Core_NLP
#if [ $LANG == "arabic" ]
time java -mx2g -cp "*" edu.stanford.nlp.pipeline.StanfordCoreNLP -props StanfordCoreNLP-${LANG}.properties -file ${INPUT_FILE} -outputDirectory ${TEST_PARSED_FILE} -outputFormat conllu
time java -mx2g -cp "*" edu.stanford.nlp.pipeline.StanfordCoreNLP -props StanfordCoreNLP-${LANG}.properties -annotators tokenize,ssplit -file ${INPUT_FILE} -outputDirectory ${ALIGNMENT_FILE} -outputFormat json
echo "Finished running CoreNLP."
echo "**********************************************************************"
echo
echo