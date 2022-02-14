# ProjetMI
Projet Modalité d'interaction avec fusion modale


Dessin sur Palette en mode Apprentissage
Supprimer toutes les formes et aucun dessin en mode Reconnaissance

Créer la commande en fusionnant le vocal + la gestuel

-------- &&& ---------
Créer objet [Position] [Couleur]
 La désignation de l’action (créer un rectangle ou une ellipse) se fera à l’aide
d’un geste 2D. Un geste différent peut être prévu pour chaque type d’objet
(rectangle ou ellipse);

 La désignation de la position et de la couleur est optionnelle. Ils peuvent être
présents dans n’importe quel ordre, mais forcément après la désignation de
l’action ;

 La position devra être spécifiée par un mot clé donné à la voix (« ici », « là »,
« à cette position ») et complétée par un pointage sur la palette désignant la
position.

 La couleur pourra être définie soit en donnant une couleur à la voix
(« rouge », « noir », « vert », etc.) ; soit en désignant sur la palette un autre
objet et en annonçant à la voix « de cette couleur ».

-------- &&& ---------
 Supprimer Objet [Couleur]
 La désignation de l’action (supprimer) se fera à l’aide d’un geste 2D ;

 La désignation de l’objet se fera par le pointage de l’objet à supprimer, et
complété par la désignation à la voix de l’objet (« cet objet », « ce rectangle »
ou « cette ellipse » pour être plus précis) ;

 L’annonce de la couleur de l’objet est optionnelle. Elle servira dans le cas où
deux objets de même nature seraient imbriqués. La couleur donnerait alors
une information supplémentaire pour désambiguïser la désignation de l’objet.
Si elle a lieu, ce sera à la voix en annonçant la couleur de l’objet à supprimer.

-------- &&& ---------
 Déplacer [Objet] [Position]
 La désignation de l’action (déplacer) se fera à l’aide d’un geste 2D ;

 La désignation de l’objet et de la position est obligatoire. Ils peuvent être
présents dans n’importe quel ordre, mais forcément après la désignation de
l’action ;

 La désignation de l’objet se fera par le pointage de l’objet à déplacer, et 
complété à la voix par la désignation de l’objet (« cet objet », « ce rectangle »
ou « cette ellipse » pour être plus précis). Cette désignation pourra aussi être
complétée par la couleur de l’objet à déplacer (à la voix) ;

 La position devra être spécifiée par un mot clé à la voix (« ici », « là », « à cette
position ») et complétée par un pointage sur la palette désignant la position

-------- &&& ---------
Pour le dimanche 27 Février au plus tard, vous devez envoyer :
 un document d’une dizaine de pages contenant :
o le langage d'interaction utilisé pour la reconnaissance de geste et de parole,
o vos choix de conception (ordre des instructions, gestion du temps, CARE, etc.),
o une description de la structure de données utilisée,
o le contrôleur de dialogue (liste des événements, liste des actions, automate)
o un exemple d’utilisation de votre application.

 Une vidéo contenant une succession de séquences illustrant le fonctionnement du
moteur de fusion dans les différents cas envisagés

Le document, les vidéos et les codes sources sont à envoyer par mail à
mathieu.raynal@gmail.com. 