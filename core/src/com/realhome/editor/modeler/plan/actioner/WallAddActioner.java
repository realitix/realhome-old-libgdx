package com.realhome.editor.modeler.plan.actioner;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;
import com.realhome.editor.modeler.plan.renderer.wall_button.WallButtonRenderer;

public class WallAddActioner extends BaseActioner {
	public static final String NAME = "WallAddActioner";
	private boolean enabled = false;
	private Wall lastWall = null;
	private final Vector2 lastLocation = new Vector2();
	private final Vector2 delta = new Vector2();
	private final Array<Point> tmpPoints = new Array<Point>();

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void enable () {
		enabled = true;
	}
	
	private boolean inLastPoint(int x, int y) {
		if(lastWall == null) return false;
		
		int w = lastWall.getWidth()/2;
		Point p = lastWall.getPoints()[0];
		
		if(x >= p.x - w && x <= p.x + w && y >= p.y - w && y <= p.y + w) return true;
		return false;
	}

	@Override
	public boolean move (int x, int y) {
		if(!enabled || lastWall == null) return false;

		delta.set(lastLocation.x - x, lastLocation.y - y).scl(-1);

		tmpPoints.clear();
		tmpPoints.add(lastWall.getPoints()[1]);
		int posX = (int)(tmpPoints.get(0).x + delta.x);
		int posY = (int)(tmpPoints.get(0).y + delta.y);
		interactor.movePoints(tmpPoints, posX, posY);


		lastLocation.set(x, y);

		return false;
	}

	@Override
	public boolean click (int x, int y) {
		if(!enabled) return false;
		
		if(inLastPoint(x, y)) {
			interactor.deleteWall(lastWall);
			interactor.disableWallButton();
			enabled = false;
			lastWall = null;
			return true;
		}

		boolean lastWallNull = (lastWall == null);
		
		lastWall = interactor.addWall(new Point(x, y));
		
		if(lastWallNull)
			interactor.setWallButton(WallButtonRenderer.CANCEL, lastWall.getPoints()[0].x, lastWall.getPoints()[0].y, lastWall.getWidth());
		else
			interactor.setWallButton(WallButtonRenderer.CHECK, lastWall.getPoints()[0].x, lastWall.getPoints()[0].y, lastWall.getWidth());

		lastLocation.set(x, y);
		return true;
	}

	@Override
	public boolean unclick (int x, int y) {
		if(!enabled) return false;
		return false;
	}
}
