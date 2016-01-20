package com.realhome.editor.modeler.plan.interactor;

import java.util.Comparator;
import java.util.Stack;
import java.util.TreeSet;

import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class OutlineInteractor {

	private final Interactor interactor;

	public OutlineInteractor(Interactor interactor) {
		this.interactor = interactor;
	}

	public void compute() {
		Array<Point> points = getWallsPoints();
		Array<Point> outlinePoints = interactor.getHousePlan().getOutlinePoints();

		outlinePoints.clear();
		if(points.size > 0)
			outlinePoints.addAll(GrahamScan.getConvexHull(points));
	}

	private Array<Point> getWallsPoints() {
		Array<Point> points = new Array<Point>();
		for(WallPlan w : interactor.getHousePlan().getWalls()) {
			for(Point p : w.getPoints()) {
				points.add(new Point(p));
			}
		}

		return points;
	}

	private static class GrahamScan {
		private static enum Turn { CLOCKWISE, COUNTER_CLOCKWISE, COLLINEAR }

		private static boolean areAllCollinear(Array<Point> points) {
			if(points.size < 2) {
				return true;
			}

			final Point a = points.get(0);
			final Point b = points.get(1);

			for(int i = 2; i < points.size; i++) {
				Point c = points.get(i);

				if(getTurn(a, b, c) != Turn.COLLINEAR) {
					return false;
				}
			}
			return true;
		}

		public static Array<Point> getConvexHull(Array<Point> points) throws IllegalArgumentException {
			Array<Point> sorted = new Array<Point>(getSortedPointSet(points));

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

			Stack<Point> stack = new Stack<Point>();
			stack.push(sorted.get(0));
			stack.push(sorted.get(1));

			for (int i = 2; i < sorted.size; i++) {
				Point head = sorted.get(i);
				Point middle = stack.pop();
				Point tail = stack.peek();

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

			Array<Point> result = new Array<Point>();
			for(Point point : stack) {
				result.add(point);
			}

			return result;
		}

		private static Point getLowestPoint(Array<Point> points) {
			Point lowest = points.get(0);

			for(int i = 1; i < points.size; i++) {
				Point temp = points.get(i);
				if(temp.y < lowest.y || (temp.y == lowest.y && temp.x < lowest.x)) {
					lowest = temp;
				}
			}
			return lowest;
		}

		private static Array<Point> getSortedPointSet(Array<Point> points) {
			final Point lowest = getLowestPoint(points);

			TreeSet<Point> set = new TreeSet<Point>(new Comparator<Point>() {
				@Override
				public int compare(Point a, Point b) {

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
						// collinear with the 'lowest' point, let the point closest to it come first

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

			Array<Point> result = new Array<Point>();
			for(Point point : set) {
				result.add(point);
			}
			return result;
		}

		private static Turn getTurn(Point a, Point b, Point c) {

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
