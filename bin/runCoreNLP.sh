#!/bin/bash

set -e #fail last

MY_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" > /dev/null && pwd )"

source "${MY_DIR}/config.sh"

if [ $# -lt 2 -o $# -gt 2 ]; then
   echo "USAGE: `basename "${0}"` <input-file> <output-dir>"
   exit 1
fi

# location of input file. must be absolute path
INPUT_FILE="${SEMAFOR_HOME}/data/${1}"

# where to write the output
OUTPUT_DIR="${2}"

LANG="${3}"

TEST_PARSED_FILE="${OUTPUT_DIR}/conll"

echo "**********************************************************************"
echo "Running CoreNLP...."
#-props StanfordCoreNLP-arabic.properties
pushd ${SEMAFOR_HOME}/Core_NLP
time java -mx2g -cp "*" edu.stanford.nlp.pipeline.StanfordCoreNLP -props StanfordCoreNLP-${LANG}.properties -file ${INPUT_FILE} -outputDirectory ${TEST_PARSED_FILE} -outputFormat conllu
echo "Finished running CoreNLP."
echo "**********************************************************************"
echo
echo