#!/bin/bash

set -e

MY_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" > /dev/null && pwd )"

if [ $1 != "a" ] && [ $1 != "c" ] && [ $1 != "e" ] && [ $1 != "f" ] && [ $1 != "g" ] && [ $1 != "s" ] || [ -z $1 ]
then
	echo "USAGE = `basename "${0}"` <language>"
	echo "language = [a=arabic] [c=chinese] [e=english] [f=french] [g=german] [s=spanish]"
	exit 1
else
	case $1 in
		"a")
			echo "chosen language = arabic"
			URL_PARS="http://nlp.stanford.edu/software/stanford-arabic-corenlp-2018-02-27-models.jar"
			;;
		"c")
			echo "chosen language = chinese"
			URL_PARS="http://nlp.stanford.edu/software/stanford-chinese-corenlp-2018-02-27-models.jar"
			;;
		"e")
			echo "chosen language = english"
			URL_PARS="http://nlp.stanford.edu/software/stanford-english-corenlp-2018-02-27-models.jar"
			;;
		"f")
			echo "chosen language = french"
			URL_PARS="http://nlp.stanford.edu/software/stanford-french-corenlp-2018-02-27-models.jar"
			;;
		"g")
			echo "chosen language = german"
			URL_PARS="http://nlp.stanford.edu/software/stanford-german-corenlp-2018-02-27-models.jar"
			;;
		"s")
			echo "chosen langage = spanish"
			URL_PARS="http://nlp.stanford.edu/software/stanford-spanish-corenlp-2018-02-27-models.jar"
			;;
	esac
fi

source "${MY_DIR}/config.sh"



sudo apt -y install wget
#sudo apt -y install maven
sudo apt -y install tar

#mvn package

DIR_PARS="${SEMAFOR_HOME}/Core_NLP"

pushd $DIR_PARS
wget $URL_PARS --progress=bar

mkdir -p $SEMAFOR_HOME/models
DIR_SEM_MOD="${SEMAFOR_HOME}/models"

URL_SEM_MOD="http://www.ark.cs.cmu.edu/SEMAFOR/semafor_malt_model_20121129.tar.gz"

pushd $DIR_SEM_MOD
wget $URL_SEM_MOD --progress=bar

tar -xf  semafor_malt_model_20121129.tar.gz
