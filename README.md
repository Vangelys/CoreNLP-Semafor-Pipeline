# Internship project : pipeline between Stanford Core NLP and Semafor

*1st may / 20 july - Guilherme RAZET, LORIA, Nancy*

## Setup

1. Clone the repository ( [link](https://github.com/Vangelys/CoreNLP-Semafor-Pipeline.git));

2. Open a terminal in CoreNLP-Semafor-Pipeline;

3. Run this command : `./bin/install.sh [language]` with `language = [a=arabic] [c=chinese] [e=english] [f=french] [g=german] [s=spanish]`;

4. Check environnements variables in `bin/config.sh`;

The pipeline is ready !

## Use

1. Place your document in the folder `data` (this document must be a `.txt`);

2. Open a terminal in CoreNLP-Semafor-Pipeline;

3. Run this command :`.bin/Semafor.sh [input] [output] [number of threads] [language]`, with :
	
	1. `[input]` = name of your document (exemple : `test.txt`);

	2. `[output]` = path and name of your output (exemple : `data/test.out.xml`). **CAUTION** : this document must be in `.xml` and it must not exist;

	3. `[number of threads]` = number of threads used in the process, usually at least 2;

	4. `[language]` = language of the document, with `[a=arabic] [c=chinese] [e=english] [f=french] [g=german] [s=spanish]`. **CAUTION** : the language package must be download before the process, usually during the install.

This process need 2mg of free RAM, else it will not work.

## Documentation

Documentation of Stanford Core NLP : click [here](https://stanfordnlp.github.io/CoreNLP/index.html).

Documentation of Semafor : click [here](https://github.com/Noahs-ARK/semafor).