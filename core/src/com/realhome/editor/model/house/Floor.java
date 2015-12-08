
package com.realhome.editor.model.house;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Floor {
	private Array<Wall> walls = new Array<Wall>();

	public Array<Wall> getWalls () {
		return walls;
	}

	public Floor addWall (Wall wall) {
		walls.add(wall);
		return this;
	}

	public Floor removeWall (Wall wall) {
		walls.removeValue(wall, true);
		return this;
	}

	private Array<Wall> cachedWalls = new Array<Wall>();
	private Array<WallGroup> groups = new Array<WallGroup>();

	public Array<Vector2Path> getPaths () {
		Array<Vector2Path> paths = new Array<Vector2Path>();
		Array<WallGroup> groups = getWallGroups();
		for (WallGroup group : groups) {
			paths.add(removeDuplicate(getPathFromGroup(group)));
		}
		return paths;
	}

	private Vector2Path removeDuplicate (Vector2Path path) {
		int i = 0;

		while (i < path.size) {
			if (i < path.size - 1 && path.get(i).equals(path.get(i + 1))) {
				path.removeIndex(i + 1);
				i = -1;
			}
			i++;
		}

		return path;
	}

	private Vector2Path getPathFromGroup (WallGroup group) {
		Vector2Path path = new Vector2Path();
		StartWallResult startWall = findBorderWallInGroup(group);
		cachedWalls.clear();
		extractPoints(group, startWall.wall, startWall.point, path);
		return path;
	}

	private void extractPoints (WallGroup group, Wall wall, Vector2 startPoint, Vector2Path path) {
		if (startPoint != wall.getPoint0() && startPoint != wall.getPoint1()) throw new GdxRuntimeException("Impossible!");

		Vector2 endPoint = (wall.getPoint0() == startPoint) ? wall.getPoint1() : wall.getPoint0();
		path.add(startPoint);
		path.add(endPoint);

		cachedWalls.add(wall);
		for (int i = 0; i < walls.size; i++) {
			Wall w = walls.get(i);
			if (cachedWalls.contains(w, true)) continue;
			if (w.getPoint0().equals(endPoint)) {
				extractPoints(group, w, w.getPoint0(), path);
			} else if (w.getPoint1().equals(endPoint)) {
				extractPoints(group, w, w.getPoint1(), path);
			}
		}
	}

	private StartWallResult findBorderWallInGroup (WallGroup group) {
		StartWallResult result = new StartWallResult();
		Array<Vector2> points = new Array<Vector2>();

		for (Wall wall : group) {
			points.add(wall.getPoint0());
			points.add(wall.getPoint1());
		}

		boolean duplicate;
		for (int i = 0; i < points.size; i++) {
			duplicate = false;
			for (int j = 0; j < points.size; j++) {
				if (points.get(i) != points.get(j) && points.get(i).equals(points.get(j))) {
					duplicate = true;
				}
			}

			// return the wall which point belongs to
			if (!duplicate) {
				Vector2 p = points.get(i);
				result.point = p;
				for (Wall wall : group) {
					if (wall.getPoint0() == p || wall.getPoint1() == p) {
						result.wall = wall;
						return result;
					}
				}
			}
		}
		result.wall = group.get(0);
		result.point = result.wall.getPoint0();
		return result;
	}

	public Array<WallGroup> getWallGroups () {
		cachedWalls.clear();
		groups.clear();

		for (int i = 0; i < walls.size; i++) {
			if (cachedWalls.contains(walls.get(i), true)) continue;
			WallGroup group = new WallGroup();
			findWallsInGroup(walls.get(i), group);
			groups.add(group);
		}

		return groups;
	}

	private void findWallsInGroup (Wall wall, WallGroup wallGroup) {
		cachedWalls.add(wall);
		wallGroup.add(wall);
		for (int i = 0; i < walls.size; i++) {
			if (cachedWalls.contains(walls.get(i), true)) continue;
			if (wall.isLinked(walls.get(i))) {
				findWallsInGroup(walls.get(i), wallGroup);
			}
		}
	}

	public static class WallGroup extends Array<Wall> {
	}

	public static class Vector2Path extends Array<Vector2> {
		public boolean isOpen () {
			if (get(0).equals(get(size - 1))) return false;
			return true;
		}
	}

	public static class StartWallResult {
		public Wall wall;
		public Vector2 point;
	}
}
