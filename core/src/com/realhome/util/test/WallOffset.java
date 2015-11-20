public class WallOffset {
	private Array<Wall> walls;
	/**
	 * Met à jour les points de tous les murs
	*/
	public void updateWalls(Array<Wall> walls) {
		this.walls = walls;
		for(int i = 0; i < walls.size; i++) {
			updateWall(walls.get(i));
		}
	}

	/**
	 * Met à jour les points d'un mur
	*/
	public void updateWall(Wall wall) {
		for( int i = 0; i < wall.getPoints().size; i++) {}
			updatePoint(wall, wall.getPoints().get(i));
		}
	}

	/**
	 * Met à jour un point sur les deux du mur
	*/
	private void updatePoint(Wall wall, Vector2 point) {
		// Get actual associate 2D points
		Vector2[] extrusionPoints = wall.getAssociate2D(point);
		boolean linkedWallFinded = false;

		for(int i = 0; i < walls.size; i++) {
			// w is the current tested wall
			Wall w = walls.get(i);
			if(wall == w) continue;

			// for each points in tested wall
			for(int j = 0; j < w.getPoints().size; j++) {
				// p is the current tested point
				Vector2 p = w.getPoints().get(j);

				if(p.equals(point)) {
					updateIntersectionExtrusionPoints(wall, w);
					linkedWallFinded = true;
				}
			}
		}

		if ( !linkedWallFinded ) {
			updateSimpleExtrusionPoints(wall, point);
		}
	}
}