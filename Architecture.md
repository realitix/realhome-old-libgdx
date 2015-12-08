MVC + Command + Notification

La vue contient un Actor.

Le controlleur test à chaque tour si l'Actor est cliqué. Si cliqué, il exécute une commande avec le CommandPattern. La commande pouvant avoir besoin d'argument innaccessible dans le controlleur, c'est le commandManager qui ca lancer la commande. Pour ce faire, le controleur lance une notification d'exécution de commande, le commandManager peut ensuite lancer la commande avec les arguments (le commandManager possède une vue sur tous les modèle de l'appli).

Le modèle ne connait rien de son environnement. La vue ne connait rien non plus. Les commandes modifient le modèle et lance ensuite une notification pour dire que le modèle est modifié. Les controleur écoutent les notification et peuvent agir s'ils sont intéressé.

Le controleur ne modifie jamais directment le modèle, il connait le modèle mais euelemnt pour faire des get dessus. Seul les commandes modifient directemnt le modèle.

Le controleur écoute les évenemnts associés à la vue lié. lors d'un évenement, il exécute une commande. Lorsque la commande est terminé, le controleur met à jour la vue avec les nouvelle données du modèles. Il peut envoyer une notification si nécessaire au lieu d'éxécuter une commande (pour demander à un autre controlleur de le faire par exemple).