MVC + Command + Notification

La vue contient un Actor.
Le controlleur test à chaque tour si l'Actor est cliqué. Si cliqué, il exécute une commande avec le CommandPattern. La commande pouvant avoir besoin d'argument innaccessible dans le controlleur, c'est le commandManager qui ca lancer la commande. Pour ce faire, le controleur lance une notification d'exécution de commande, le commandManager peut ensuite lancer la commande avec les arguments (le commandManager possède une vue sur tous les modèle de l'appli).

Le modèle ne connait rien de son environnement. La vue ne connait rien non plus. Les commandes modifient le modèle et lance ensuite une notification pour dire que le modèle est modifié. Les controleur écoutent les notification et peuvent agir s'ils sont intéressé.