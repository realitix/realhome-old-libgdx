package com.realhome.editor.modeler.plan.actioner;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.modeler.plan.actioner.util.Action;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.PointPlan;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class OverActioner implements Actioner {
	private HousePlan house;
	private boolean dragging;
	private final Array<BaseOver> overs = new Array<BaseOver>();
	private final IntArray results = new IntArray();

	@Override
	public Actioner init(HousePlan house) {
		this.house = house;
		overs.add(new PointOver());
		overs.add(new WallOver());
		
		return this;
	}

	/**
	 * We loop through all overs.
	 * We return the first not empty Action
	 */
	@Override
	public int move(int x, int y) {
		results.clear();
		
		int lastAction = Action.EMPTY;
		boolean onTop = false;
		
		for(BaseOver over : overs) {
			if( lastAction != Action.EMPTY && lastAction != Action.OVER_OUT)
				onTop = true;
			
			lastAction = over.move(x, y, onTop);
			results.add(lastAction);
		}

		for(int i = 0; i < results.size; i++) {
			if( results.get(i) != Action.EMPTY )
				return results.get(i);
		}
		
		return Action.EMPTY;
	}

	@Override
	public int click(int x, int y) {
		dragging = true;
		return Action.EMPTY;
	}

	@Override
	public int unclick(int x, int y) {
		dragging = false;
		return Action.EMPTY;
	}
	
	/**
	 * Base class for all over to handle
	 */
	private abstract class BaseOver {
		protected abstract boolean isSelected();
		protected abstract int setOverSelected();
		protected abstract int setOver(int x, int y);
		protected abstract void removeOver();
		protected abstract boolean isOverNotNull();
		
		public int move(int x, int y, boolean onTop) {
			if( isSelected() ) {
				return setOverSelected();
			}
			
			if(dragging)
				return Action.EMPTY;
			
			if(onTop) {
				removeOver();
				return Action.OVER_OUT;
			}
			
			int r;
			if((r = setOver(x, y)) != 0)
				return r;

			if( isOverNotNull() ) {
				removeOver();
				return Action.OVER_OUT;
			}

			return Action.EMPTY;
		};
	}
	
	/**
	 * Listen walls
	 */
	private class WallOver extends BaseOver {
		
		@Override
		protected boolean isSelected() {
			return house.getSelectedWall() != null;
		}
		
		@Override
		protected int setOverSelected() {
			house.setOverWall(house.getSelectedWall());
			return Action.OVER_WALL;
		}
		
		@Override
		protected void removeOver() {
			house.removeOverWall();
		}
		
		@Override
		protected int setOver(int x, int y) {
			for(WallPlan wall : house.getWalls()) {
				if(wall.pointInside(x, y)) {
					if(house.getOverWall().getWall() != wall)
						house.setOverWall(wall);
					return Action.OVER_WALL;
				}
			}
			
			return 0;
		}
		
		@Override
		protected boolean isOverNotNull() {
			return house.getOverWall().getWall() != null;
		}
	}
	
	/**
	 * Listen points
	 */
	private class PointOver extends BaseOver {
		
		@Override
		protected boolean isSelected() {
			return house.getSelectedPoint() != null;
		}
		
		@Override
		protected int setOverSelected() {
			house.setOverPoint(house.getSelectedPoint());
			return Action.OVER_POINT;
		}
		
		@Override
		protected void removeOver() {
			house.removeOverPoint();
		}
		
		@Override
		protected int setOver(int x, int y) {
			for(WallPlan wall : house.getWalls()) {
				int w = wall.getOrigin().getWidth();
				for(Point point : wall.getOrigin().getPoints()) {
					if( x <= point.x + w && x >= point.x - w && y <= point.y + w && y >= point.y - w) {
						if( house.getOverPoint().getPoint() != point ) {
							PointPlan pp = new PointPlan().setPoint(point).setWall(wall.getOrigin());
							house.setOverPoint(pp);
						}
						return Action.OVER_POINT;
					}
				}
			}
			
			return 0;
		}
		
		@Override
		protected boolean isOverNotNull() {
			return house.getOverPoint().getPointPlan() != null;
		}
	}
}