package com.realhome.editor.modeler.plan.interactor;

import java.util.Comparator;
import java.util.Stack;
import java.util.TreeSet;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class OutlineInteractor {

	private final Interactor interactor;

	public OutlineInteractor(Interactor interactor) {
		this.interactor = interactor;
	}

	public void compute() {
		Array<Vector2> points = getWallsPoints();
		Array<Vector2> outlinePoints = interactor.getHousePlan().getOutlinePoints();

		outlinePoints.clear();
		//if(points.size > 0)
		//outlinePoints.addAll(GrahamScan.getConvexHull(points));
	}

	private Array<Vector2> getWallsPoints() {
		Array<Vector2> points = new Array<Vector2>();
		for(WallPlan w : interactor.getHousePlan().getWalls()) {
			for(Vector2 p : w.getPoints()) {
				points.add(new Vector2(p));
			}
		}

		return points;
	}

	private static class GrahamScan {
		private static enum Turn { CLOCKWISE, COUNTER_CLOCKWISE, COLLINEAR }

		private static boolean areAllCollinear(Array<Vector2> points) {
			if(points.size < 2) {
				return true;
			}

			final Vector2 a = points.get(0);
			final Vector2 b = points.get(1);

			for(int i = 2; i < points.size; i++) {
				Vector2 c = points.get(i);

				if(getTurn(a, b, c) != Turn.COLLINEAR) {
					return false;
				}
			}
			return true;
		}

		public static Array<Vector2> getConvexHull(Array<Vector2> points) throws IllegalArgumentException {
			Array<Vector2> sorted = new Array<Vector2>(getSortedPointSet(points));

			if(sorted.size < 3) {
				sorted.clear();
				return sorted;
				//throw new IllegalArgumentException("can only create a convex hull of 3 or more unique points");
			}

			if(areAllCollinear(sorted)) {
				sorted.clear();
				return sorted;
				//throw new IllegalArgumentException("cannot create a convex hull from collinear points");
			}

			Stack<Vector2> stack = new Stack<Vector2>();
			stack.push(sorted.get(0));
			stack.push(sorted.get(1));

			for (int i = 2; i < sorted.size; i++) {
				Vector2 head = sorted.get(i);
				Vector2 middle = stack.pop();
				Vector2 tail = stack.peek();

				Turn turn = getTurn(tail, middle, head);

				switch(turn) {
				case COUNTER_CLOCKWISE:
					stack.push(middle);
					stack.push(head);
					break;
				case CLOCKWISE:
					i--;
					break;
				case COLLINEAR:
					stack.push(head);
					break;
				}
			}

			// close the hull
			stack.push(sorted.get(0));

			Array<Vector2> result = new Array<Vector2>();
			for(Vector2 point : stack) {
				result.add(point);
			}

			return result;
		}

		private static Vector2 getLowestPoint(Array<Vector2> points) {
			Vector2 lowest = points.get(0);

			for(int i = 1; i < points.size; i++) {
				Vector2 temp = points.get(i);
				if(temp.y < lowest.y || (temp.y == lowest.y && temp.x < lowest.x)) {
					lowest = temp;
				}
			}
			return lowest;
		}

		private static Array<Vector2> getSortedPointSet(Array<Vector2> points) {
			final Vector2 lowest = getLowestPoint(points);

			TreeSet<Vector2> set = new TreeSet<Vector2>(new Comparator<Vector2>() {
				@Override
				public int compare(Vector2 a, Vector2 b) {

					if(a == b || a.equals(b)) {
						return 0;
					}

					// use longs to guard against int-underflow
					double thetaA = Math.atan2((long)a.y - lowest.y, (long)a.x - lowest.x);
					double thetaB = Math.atan2((long)b.y - lowest.y, (long)b.x - lowest.x);

					if(thetaA < thetaB) {
						return -1;
					}
					else if(thetaA > thetaB) {
						return 1;
					}
					else {
						// collinear with the 'lowest' Vector2, let the Vector2 closest to it come first

						// use longs to guard against int-over/underflow
						double distanceA = Math.sqrt((((long)lowest.x - a.x) * ((long)lowest.x - a.x)) +
							(((long)lowest.y - a.y) * ((long)lowest.y - a.y)));
						double distanceB = Math.sqrt((((long)lowest.x - b.x) * ((long)lowest.x - b.x)) +
							(((long)lowest.y - b.y) * ((long)lowest.y - b.y)));

						if(distanceA < distanceB) {
							return -1;
						}
						else {
							return 1;
						}
					}
				}
			});

			for(int i = 0; i < points.size; i++) {
				set.add(points.get(i));
			}

			Array<Vector2> result = new Array<Vector2>();
			for(Vector2 point : set) {
				result.add(point);
			}
			return result;
		}

		private static Turn getTurn(Vector2 a, Vector2 b, Vector2 c) {

			// use longs to guard against int-over/underflow
			long bax = (long)b.x - (long)a.x;
			long bay = (long)b.y - (long)a.y;

			long cax = (long)c.x - (long)a.x;
			long cay = (long)c.y - (long)a.y;

			long crossProduct = (bax * cay) - (bay * cax);

			if(crossProduct > 0) {
				return Turn.COUNTER_CLOCKWISE;
			}
			else if(crossProduct < 0) {
				return Turn.CLOCKWISE;
			}
			else {
				return Turn.COLLINEAR;
			}
		}
	}
}
