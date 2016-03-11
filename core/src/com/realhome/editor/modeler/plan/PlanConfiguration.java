package com.realhome.editor.modeler.plan;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class PlanConfiguration {

	public static class World {
		public static float height = 1000;
	}

	public static class Arc {
		public static float size = 120;
		public static float bubbleSize = 30;
		public static float outlineSize = 2;
		public static Color color = new Color(0, 0, 0, 0.2f);
		public static Color bubbleColor = new Color(1, 1, 1, 1);
		public static Color outlineColor = new Color(0.53f, 0.72f, 0.03f, 1);
	}

	public static class Grid {
		public static Color color = new Color(0.5f, 0.5f, 0.5f, 0.5f);
		public static final int width = 5000;
		public static final int height = 5000;
		public static final int tileSize = 50;
	}

	public static class Label {
		public static Color color = new Color(0.2f, 0.2f, 0.2f, 1);
		public static float scale = 0.7f;
	}

	public static class Mask {
		public static Color color = new Color(1, 1, 1, 1);
	}

	public static class Room {
		public static Color color = new Color(1, 0f, 0, 1);
	}

	public static class Measure {
		public static Color color = new Color(0.5f, 0.5f, 0.5f, 0.5f);
		public static int gap = 25;
		public static float arrowAngle = 45;
		public static float arrowSize = 10;
		public static float labelOffset = 14;
	}

	public static class OverPoint {
		public static Color color = new Color(0.53f, 0.72f, 0.03f, 1);
		public static int circleSize = 7;
		public static int borderSize = 4;
	}

	public static class OverWall {
		public static int circleSize = 7;
		public static int lineSize = 3;
		public static Color circleColor = new Color(0.53f, 0.72f, 0.03f, 1);
		public static Color lineColor = new Color(0.53f, 0.72f, 0.03f, 1);
	}

	public static class Wall {
		public static Color backgroundColor = new Color(0.2f, 0.2f, 0.2f, 1);
		public static Color lineColor = new Color(0.39f, 0.39f, 0.39f, 1f);
		public static float lineWidth = 0.08f;
		public static Vector2 tile = new Vector2(0.05f, 0.05f);
		public static float referenceSize = 500;
	}
}
