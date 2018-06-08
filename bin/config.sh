#!/bin/sh
######################## ENVIRONMENT VARIABLES ###############################
######### change the following according to your own local setup #############


# assumes this script (config.sh) lives in "${BASE_DIR}/semafor/bin/"
export BASE_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/../.." > /dev/null && pwd )"
# path to the absolute path
# where you decompressed SEMAFOR.
export SEMAFOR_HOME="${BASE_DIR}/semafor-corenlp-pipeline"

export CLASSPATH=".:${SEMAFOR_HOME}/target/Semafor-3.0-alpha-04.jar"
#:/home/guilherme/.m2/repository/commons-io/commons-io/2.4/commons-io-2.4.jar:/home/guilherme/git_repository/CoreNLP/src/edu/stanford/nlp/process/Morpha.java:${SEMAFOR_HOME}/target/morpha-stemmer-1.0.3.jar:/home/guilherme/.m2/repository/org/pcollections/pcollections/2.1.2/pcollections-2.1.2.jar

# Change the following to the bin directory of your $JAVA_HOME
export JAVA_HOME_BIN="/usr/bin"

# Change the following to the directory where you decompressed 
# the models for SEMAFOR 2.0.
export MALT_MODEL_DIR="${SEMAFOR_HOME}/models/semafor_malt_model_20121129"
export TURBO_MODEL_DIR="${SEMAFOR_HOME}/models/turbo_20130606"



######################## END ENVIRONMENT VARIABLES #########################

echo "Environment variables:"
echo "SEMAFOR_HOME=${SEMAFOR_HOME}"
echo "CLASSPATH=${CLASSPATH}"
echo "JAVA_HOME_BIN=${JAVA_HOME_BIN}"
echo "MALT_MODEL_DIR=${MALT_MODEL_DIR}"
