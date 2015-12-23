package com.realhome.editor.modeler.plan.actioner;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class WallMovingActioner implements Actioner {
	private HousePlan house;
	private final Point tmp = new Point();
	private final Vector2 lastLocation = new Vector2();
	private final Vector2 delta = new Vector2();
	private final Array<Point> tmpPoints = new Array<Point>();

	@Override
	public Actioner init (HousePlan house) {
		this.house = house;
		return this;
	}

	@Override
	public int move (int x, int y) {
		if(house.getSelectedWall() == null)
			return Action.EMPTY;

		delta.set(lastLocation.x - x, lastLocation.y - y).scl(-1);
		moveWallDelta((int) -(lastLocation.x - x), (int) -(lastLocation.y - y), getCommonCorners());
		lastLocation.set(x, y);

		return Action.MOVE_WALL;
	}
	
	/**
	 * Return the number of common point in corner.
	 * 0, 1, 2
	 * @return
	 */
	private int getCommonCorners() {
		int result = 0;
		
		Wall wallSource = house.getSelectedWall().getOrigin();
		for(Point sourcePoint : wallSource.getPoints()) {
			for(WallPlan w : house.getWalls()) {
				Wall wallTarget = w.getOrigin();
				if ( wallTarget == wallSource ) continue;

				for(Point targetPoint : wallTarget.getPoints()) {
					if(sourcePoint.equals(targetPoint))
						result++;
				}
			}
		}
		return result;
	}

	/**
	 * Algo:
	 * On créé deux points de tests associé au mur sélectionné
	 * On déplace ces points en ajoutant le delta
	 * Pour chaque mur (targetWall) qui a un point en commun avec le mur sélectionné
	 * On stocke une référence vers le point de targetWall et une référence vers le mur sélectionné
	 * On cherche le croisement entre targetWall et la ligne formé par les deux premiers points
	 * On set les points de targetWall et sourceWall à ce point
	 *
	 */
	private void moveWallDelta(int x, int y, int nbCorners) {
		// Init virtual points
		Wall wallSource = house.getSelectedWall().getOrigin();
		
		if(nbCorners == 0) {
			for(Point p : wallSource.getPoints()) p.add(x, y);
			return;
		}
		
		Point[] virtualPoints = {
			new Point(wallSource.getPoints()[0]),
			new Point(wallSource.getPoints()[1])};

		// Move virtual points
		for(Point p : virtualPoints) p.add(x, y);

		// Find linked walls
		for(Point sourcePoint : wallSource.getPoints()) {
			for(WallPlan w : house.getWalls()) {
				Wall wallTarget = w.getOrigin();
				if ( wallTarget == wallSource ) continue;

				for(Point targetPoint : wallTarget.getPoints()) {
					if(sourcePoint.equals(targetPoint)) {
						Vector2 intersection = new Vector2();
						Intersector.intersectLines(
							virtualPoints[0].x, virtualPoints[0].y,
							virtualPoints[1].x, virtualPoints[1].y,
							wallTarget.getPoints()[0].x, wallTarget.getPoints()[0].y,
							wallTarget.getPoints()[1].x, wallTarget.getPoints()[1].y,
							intersection);
						
						int posX = Math.round(intersection.x);
						int posY = Math.round(intersection.y);
						
						// If only one common point, we add the same delta to the other one
						if( nbCorners == 1 ) {
							Point otherPoint = (wallSource.getPoints()[0] == sourcePoint) ? wallSource.getPoints()[1] : wallSource.getPoints()[0];
							otherPoint.sub(sourcePoint.x - posX, sourcePoint.y - posY);
						}
						
						sourcePoint.set(posX, posY);
						targetPoint.set(sourcePoint);
					}
				}
			}
		}
	}

	@Override
	public int click (int x, int y) {
		tmp.set(x, y);
		lastLocation.set(x, y);

		for(WallPlan wall : house.getWalls()) {
			if( wall.pointInside(tmp.x, tmp.y) ) {
				house.setSelectedWall(wall);
				return Action.SELECT_WALL;
			}
		}

		return Action.EMPTY;
	}

	@Override
	public int unclick (int x, int y) {
		if(house.getSelectedWall() != null) {
			house.setSelectedWall(null);
			return Action.UNSELECT_WALL;
		}

		return Action.EMPTY;
	}
}
