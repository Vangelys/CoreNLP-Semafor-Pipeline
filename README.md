# Stage Guilherme RAZET : Pipeline parser-Semafor

# Méthode de création du package

## Installation Semafor

### SITES UTILES

[Github :](https://github.com/Noahs-ARK/semafor)

[Outils pour les formats](https://github.com/UniversalDependencies/tools)

----------------------------------------------------------------
### INSTALLATION

Semafor n'est plus maintenu depuis 2012, il reste néanmoins la solution la plus efficace en termes de résultats. L'installation peut se montrer laborieuse :

1. Télécharger le contenu du dépôt git de Semafor

2. utiliser Maven pour compiler le projet (avec la commande `mvn package`)

3. télécharger les [modèles suivants](http://www.ark.cs.cmu.edu/SEMAFOR/semafor_malt_model_20121129.tar.gz) et placer le contenu de l'archive dans un dossier "models" à la racine de Semafor

À partir de là, 2 cas de figures :

1. si ça compile bien, tant mieux, Semafor est prêt à être utilisé !

2. sinon, une solution consiste à installer toutes les dépendances à la main, depuis le dépôt de [maven](https://repo.maven.apache.org/maven2/). Quand c'est fait, relancer la commande de maven (cf annexe 1)

----------------------------------------------------------------
### UTILISATION

SEMAFOR nécessite un fichier CONLLU en entrée (pour la conversion des formats, voir SITES UTILES). Il est possible de le coupler avec un parser produisant ce type de fichier, ici le StanfordCoreNLP. Son utilisation se fait via un script, présent dans le dossier bin de Semafor.

La commande pour lancer le script (se placer dans le dossier Semafor): `.bin/runSemafor.sh [input] [output] [number of threads]`

1. le format d'input est un .txt

2. le fichier d'output ne doit pas exister, il sera créé par Semafor; le format d'output est xml

3. le nombre de threads utilisés influe sur la vitesse de traitement, il est recommandé de mettre au moins 2

----------------------------------------------------------------
## Installation StanfordCoreNLP

### SITES UTILES :

[Documentation officielle](https://stanfordnlp.github.io/CoreNLP/index.html)

----------------------------------------------------------------
### INSTALLATION :

le parser est disponible sous 2 formes : soit au sein de StanfordCoreNLP, soit en temps que StanfordParser. Je privilégie pour l'instant le StanfordCoreNLP, car il est plus complet en termes de fonctionnalités proposées.


1. télécharger le [StanfordCoreNLP](https://stanfordnlp.github.io/CoreNLP/index.html#download) ainsi que le package linguistique voulu (ici french, sous la forme .jar)

2. extraire les éléments de l'archive StanfordCoreNLP et les placer dans un dossier "CoreNLP" à la racine de Semafor

3. placer le .jar linguistique dans le dossier stanford-corenlp-full-[DATE_VERSION]


StanfordCoreNLP est prêt à être utilisé.

----------------------------------------------------------------
### UTILISATION :

Pour utiliser StanfordCoreNLP, il y a 2 méthodes : soit on se sert des lignes de commandes, soit on utilise les scripts. Voici un exemple de commande "basique" :


	java -cp "*" -Xmx1g edu.stanford.nlp.pipeline.StanfordCoreNLP -props StanfordCoreNLP-french.properties -annotators tokenize,ssplit,pos,depparse -file test1.txt


je vais ici détailler tous les éléments de la commande :


1. java : indique que le code est une application java

2. -cp "\*" : indique le CLASSPATH des classes à utiliser. Ici, on utilise * pour signifier qu'on importe tous les .jar présents dans le dossier

3. -Xmx1g : indique la mémoire ram allouée pour la JVM. Ici, on a alloué 1g

4. edu.stanford.nlp.pipeline.StanfordCoreNLP : chemin de la classe à lancer

5. -props StanfordCoreNLP-french.properties : obligatoire pour travailler sur une autre langue que l'anglais. il se trouve dans le .jar linguistique, et spécifie les classes à utiliser en fonction de la langue

6. -annotators tokenize,ssplit,pos,depparse : on retrouve dans annotators les différentes fonctions qu'on souhaite appliquer. Ici, nous avons :
	-tokenize : tokenisation du texte
	-ssplit : séparation du texte en phrase
	-pos : POS-Tagging du texte
	-depparse : parser en fonction des dépendances, c'est la fonction qui nous intéresse. ATTENTION : cette fonction a besoin des 3 précédentes pour fonctionner !!!

7. -file test1.txt : chemin du fichier à traiter

----------------------------------------------------------------
### LISTE DES COMMANDES :

[Documentation](https://stanfordnlp.github.io/CoreNLP/cmdline.html)

----------------------------------------------------------------
## Utilisation du Pipeline

1. installer les deux outils (cf installation)

2. ouvrir un terminal dans le dossier Semafor

3. exécuter la commande `.bin/Semafor.sh [input] [output] [number of threads]`

exemple de commande : `./bin/runSemafor.sh 10par.txt /home/guilherme/git_repository/semafor/10par.out.xml 2`

----------------------------------------------------------------
## Annexe 1 : Installation des dépendances de maven

Voici la liste des instructions à répéter tant que le projet ne compile pas correctement :

1. exécuter la commande `mvn package` à la racine de Semafor, pour obtenir une dépendance manquante

2. chercher celle-ci dans le dépôt officiel de [maven](https://repo.maven.apache.org/maven2/), puis remonter l'arborescence donnée par l'erreur (exemple : org/apache/maven/plugins/maven-dependency-plugin/2.8)

3. copier l'intégralité des documents présents sur la page dans le dossier correspondant (par défaut /home/[user]/.m2/repository/[arborescence]/[nom]/[version]/)

4. vérifier que la dépendance est bien inscrite dans le document pom.xml, à la racine de Semafor : 

	`<dependency>
	    <groupId>[arborescence]</groupId>
	    <artifactId>[om]</artifactId>
	    <version>2.2.0</version>
	</dependency>`

Si elle n'y est pas, il faut copier cette syntaxe et la placer entre les balises `<dependencies>`

# Installation depuis le dépôt GitHub# CoreNLP-Semafor-Pipeline
