# Idées logs / stats :

## Logs :

1. Print les commandes effectuées en console + étapes intermédiaires (ce qui est affiché en console); --> ajouter `2> log_pipeline.txt` à la fin de la commande

2. Temps de traitement total --> ajouter `time`  devant la commande

## Stats :

1. nombre de frames reconnues --> DONE

2. classement des frames les mieux analysées (scores ?) --> DONE

3. fournir un document avec les phrases normales et les frames analysées --> PyLaTeX, longtables [click here](https://jeltef.github.io/PyLaTeX/current/) --> DONE
	
	*John and Mary are friends.*
	*{"frames":[{"target":{"name":"Personal_relationship","spans":[{"start":4,"end":5,"text":"friends"}]},"annotationSets":[{"rank":0,"score":38.76513247823503,"frameElements":[]}]}],"tokens":["John","and","Mary","are","friends","."]}*

4. stats du fichier d'entrée
	nb phrases --> nb de lignes du JSON de sortie --> DONE
	tokens --> présent dans le JSON --> DONE 
	longueur moyenne des phrases --> "compter" le nombre d'éléments présents dans token --> DONE
	distribution en fonction de la longueur --> étude du JSON ? déjà parser, juste à "compter" le nombre d'éléments dans tokens, puis faire les stats dessu

# pyLaTeX

## Installation

`pip install pylatex`
`pip install pylatex[matplotlib]` pour certaines fonctions

