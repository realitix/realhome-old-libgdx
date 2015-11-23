
OBTENIR_TOUS_LES_POINTS:
	Pour chaque mur (mur pincipal):
		OBTENIR_4_POINTS_DU_MUR(mur principal)

OBTENIR_4_POINTS_DU_MUR(mur_principal):
	Pour chaque points du mur principal:
		Création de deux points nuls (points_extrusion[2])
		Pour chaque mur (mur testé) (excepté le mur principal):
			Pour chaque points du mur testé:
				Si le point du mur testé est à la même position que le point du mur principal:
					point_extrusion : RECUPERE_LES_DEUX_POINTS_INTERSECTION_MURS(mur principal, mur testé)
		SI points_extrusion sont nuls (il n y a pas de mur avec des points en commun):
			point_extrusion : RECUPERE_POINTS_EXTRUSION(mur principal, point du mur)

RECUPERE_LES_DEUX_POINTS_INTERSECTION_MURS(mur1, mur2):
	points_extrusion_mur1_cote0 : RECUPERE_POINTS_EXTRUSION(mur1, cote0)
	points_extrusion_mur1_cote1 : RECUPERE_POINTS_EXTRUSION(mur1, cote1)
	points_extrusion_mur2_cote0 : RECUPERE_POINTS_EXTRUSION(mur2, cote0)
	points_extrusion_mur2_cote1 : RECUPERE_POINTS_EXTRUSION(mur2, cote1)

	mur1_droite : mur1.point1 - mur1.point0
	mur1_cote0_droite : points_extrusion_mur1_cote0.point1 - points_extrusion_mur1_cote0.point0
	mur1_cote1_droite : points_extrusion_mur1_cote1.point1 - points_extrusion_mur1_cote1.point0

	mur2_droite : mur2.point1 - mur2.point0
	mur2_cote0_droite : points_extrusion_mur2_cote0.point1 - points_extrusion_mur2_cote0.point0
	mur2_cote1_droite : points_extrusion_mur2_cote1.point1 - points_extrusion_mur2_cote1.point0

	points_resultat[2]
	Pour chaque cote (droite principale) du mur 1 (i):
		points_intersection[2]
		Pour chaque coté (droite testé) du mur 2 (j):
			point_intersection[j] : INTERSECTION_DROITES(droite_principale, droite_testé)

		// Point_le_plus_loin renvoie le point apprtenant aux deux premiers point le plus loin des deux derniers
		points_resultat[i] : INTERSECTION_NON_CROISANTE(
			POINT_LE_PLUS_LOING(points_extrusion_mur1_cote[i], points_intersection),
			points_intersection,
			AGRANDI_SEGMENT(mur2.point1, mur2.point0)
			)

	RETURN points_resultat

POINT_LE_PLUS_LOING(points_source[2], points_cible[2]):
	distance0 : LONGUEUR(points_source[0], points_cible[0]) +
				LONGUEUR(points_source[0], points_cible[1])
	distance1 : LONGUEUR(points_source[1], points_cible[0]) +
				LONGUEUR(points_source[1], points_cible[1])
	SI distance0 > distance1 :
		RETURN points_source[0]
	SINON
		RETURN points_source[1]

AGRANDI_SEGMENT(point0, point1):
	direction : point1 - point0
	point1 : point1 + direction
	point0 : point0 - direction

INTERSECTION_NON_CROISANTE(point0, point_test0, point_test1, segment_point0, segment_point1):
	 segment : SEGMENT(segment_point0, segment_point1)
	 segment_test0 : SEGMENT(point0, point_test0)
	 segment_test1 : SEGMENT(point0, point_test1)

	 SI PAS INTERSECTION(segment, segment_test0):
	 	RETURN point_test0
	 SI PAS INTERSECTION(segment, segment_test1):
	 	RETURN point_test1

RECUPERE_POINTS_EXTRUSION(mur, coté):
	direction (de 0 vers 1): NORMALISE(mur.point1 - mur.point0)
	
	SI cote == 1:
		normal : ROTATION_90(direction)
	SINON 
		normal : ROTATION_-90(direction)

	largeur: mur.largeur / 2

	point0 : mur.point0
	point1 : mur.point1

	point0 : point0 + largeur * normal
	point1 : point1 + largeur * normal

	point0 : point0 - direstion * largeur
	point1 : point1 + direstion * largeur

	RETURN point0, point1

RECUPERE_POINTS_EXTRUSION(mur, point):
	direction (de 0 vers 1): NORMALISE(mur.point1 - mur.point0)
	normal : ROTATION_90(direction)
	normal_oppose : ROTATION_90(ROTATION_90(normal))
	largeur: mur.largeur / 2

	point0 : point
	point1 : point

	point0 : point0 + largeur * normal
	point1 : point1 + largeur * normal_oppose

	RETURN point0, point1

