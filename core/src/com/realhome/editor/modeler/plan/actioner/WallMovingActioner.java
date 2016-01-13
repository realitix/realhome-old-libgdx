package com.realhome.editor.modeler.plan.actioner;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;
import com.realhome.editor.modeler.plan.interactor.Interactor;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class WallMovingActioner implements Actioner {
	private Interactor interactor;
	private final Point tmp = new Point();
	private final Vector2 lastLocation = new Vector2();
	private final Vector2 delta = new Vector2();
	private Array<Point> cachedPoints = new Array<Point>();

	@Override
	public Actioner init (Interactor interactor) {
		this.interactor = interactor;
		return this;
	}

	@Override
	public boolean move (int x, int y) {
		if(interactor.getHousePlan().getSelectedWall() == null)
			return false;

		delta.set(lastLocation.x - x, lastLocation.y - y).scl(-1);
		moveWallDelta((int) -(lastLocation.x - x), (int) -(lastLocation.y - y), getCommonCorners());
		lastLocation.set(x, y);

		return true;
	}

	/**
	 * Return the number of common point in corner.
	 * 0, 1, 2
	 * @return
	 */
	private int getCommonCorners() {
		int result = 0;

		Wall wallSource = interactor.getHousePlan().getSelectedWall().getOrigin();
		for(Point sourcePoint : wallSource.getPoints()) {
			for(WallPlan w : interactor.getHousePlan().getWalls()) {
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
		Wall wallSource = interactor.getHousePlan().getSelectedWall().getOrigin();

		if(nbCorners == 0) {
			interactor.movePoints(wallSource.getPoints(), x, y);
			return;
		}

		Point[] virtualPoints = {
			new Point(wallSource.getPoints()[0]),
			new Point(wallSource.getPoints()[1])};

		// Move virtual points
		for(Point p : virtualPoints) p.add(x, y);

		// Find linked walls
		for(Point sourcePoint : wallSource.getPoints()) {
			for(int i = 0; i < interactor.getHousePlan().getWalls().size; i++) {
				WallPlan w = interactor.getHousePlan().getWalls().get(i);
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
							cachedPoints.clear();
							cachedPoints.add(otherPoint);
							interactor.movePoints(cachedPoints, otherPoint.x - (sourcePoint.x - posX), otherPoint.y - (sourcePoint.y - posY));
						}

						cachedPoints.clear();
						cachedPoints.add(sourcePoint);
						cachedPoints.add(targetPoint);
						interactor.movePoints(cachedPoints, posX, posY);
					}
				}
			}
		}
	}

	@Override
	public boolean click (int x, int y) {
		tmp.set(x, y);
		lastLocation.set(x, y);

		for(WallPlan wall : interactor.getHousePlan().getWalls()) {
			if( wall.pointInside(tmp.x, tmp.y) ) {
				interactor.selectWall(wall);
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean unclick (int x, int y) {
		if(interactor.getHousePlan().getSelectedWall() != null) {
			interactor.selectWall(null);
			return true;
		}

		return false;
	}
}
