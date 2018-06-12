#!/bin/bash
#    The driver script for running SEMAFOR using MaltParser as its dependency parser.
#    Written by Sam Thomson (sthomson@cs.cmu.edu)
#    Based on code by Dipanjan Das (dipanjan@cs.cmu.edu)
#    Copyright (C) 2011
#    Sam Thomson
#    Language Technologies Institute, Carnegie Mellon University
#
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program.  If not, see <http://www.gnu.org/licenses/>.

set -e # fail fast


MY_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" > /dev/null && pwd )"
source "${MY_DIR}/config.sh"

if [ $4 != "a" ] && [ $4 != "c" ] && [ $4 != "e" ] && [ $4 != "f" ] && [ $4 != "g" ] && [ $4 != "s" ] || [ -z $4 ]
then
	echo "USAGE: `basename "${0}"` <input-file> <output-file> <num-threads> <language"
	echo "language = [a=arabic] [c=chinese] [e=english] [f=french] [g=german] [s=spanish]"
	exit 1
else
	case $4 in
		"a")
			echo "chosen language = arabic"
			LANG="arabic"
			;;
		"c")
			echo "chosen language = chinese"
			LANG="chinese"
			;;
		"e")
			echo "chosen language = english"
			LANG="english"
			;;
		"f")
			echo "chosen language = french"
			LANG="french"
			;;
		"g")
			echo "chosen language = german"
			LANG="german"
			;;
		"s")
			echo "chosen langage = spanish"
			LANG="spanish"
			;;
	esac
fi

# location of input file. must be absolute path
INPUT_FILE="${1}"

# where to write the output

OUTPUT_FILE="${2}"

NUM_THREADS="${3}"

TEMP_DIR="${MY_DIR}/tmp"
#TEMP_DIR=$(mktemp -d -t semafor.XXXXXXXXXX)
######################## END ENVIRONMENT VARIABLES #########################

echo "Environment variables:"
echo "SEMAFOR_HOME=${SEMAFOR_HOME}"
echo "CLASSPATH=${CLASSPATH}"
echo "JAVA_HOME_BIN=${JAVA_HOME_BIN}"
echo "MALT_MODEL_DIR=${MALT_MODEL_DIR}"
echo "TEMP_DIR: ${TEMP_DIR}"

DEPENDENCY_PARSED_FILE="${TEMP_DIR}/conll/${INPUT_FILE}.conllu"
#DEPENDENCY_PARSED_FILE="${TEMP_DIR}/dependencies.conll"

bash ${MY_DIR}/runCoreNLP.sh ${INPUT_FILE} ${TEMP_DIR} ${LANG}



echo "**********************************"
echo "Performing frame-semantic parsing."
cd ${SEMAFOR_HOME}
time ${JAVA_HOME_BIN}/java \
    -classpath ${CLASSPATH} \
    -Xms2g -Xmx2g \
    edu.cmu.cs.lti.ark.fn.Semafor \
    input-file:${DEPENDENCY_PARSED_FILE} \
    output-file:${OUTPUT_FILE} \
    model-dir:${MALT_MODEL_DIR} \
    numthreads:${NUM_THREADS}
echo "Finished frame-semantic parsing."
echo "********************************"
echo
echo

rm -r "${TEMP_DIR}"
