package com.realhome.editor.modeler.d3.builder;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Floor;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;
import com.realhome.editor.modeler.util.WallComputer;

public class WallBuilder {
	public final static String NODE_WALLS = "nodeWalls";

	public static class WallFace {
		public MeshPart meshPart;
		public Material material;

		public WallFace(MeshPart part, Material material) {
			this.meshPart = part;
			this.material = material;
		}
	}

	private HouseModelBuilder builder;
	private WallComputer computer;

	public WallBuilder(HouseModelBuilder builder) {
		this.builder = builder;
		computer = new WallComputer();
	}

	public void sync() {
		// Check walls Node
		if(builder.getHouseModel().getNode(NODE_WALLS) == null) {
			Node wallsNode = new Node();
			wallsNode.id = NODE_WALLS;
			builder.getHouseModel().nodes.add(wallsNode);
		}

		// Loop through house walls
		for(Floor floor : builder.getHouse().getFloors())
			for(int i = 0; i < floor.getWalls().size; i++) {
				Wall wall = floor.getWalls().get(i);

				// If node doesn't exists, create it
				if(!builder.hasNode(wallId(wall)))
					createNode(wall, floor.getWalls());
			}
	}

	/**
	 * Return the wall id in the node hierarchy of house model
	 * @param wall Wall to check
	 * @return String id
	 */
	private String wallId(Wall wall) {
		return "wall_" + wall.getId();
	}

	/** Create node for wall in parameter
	 * @param wall Wall to create
	 * @param walls Walls in the floor
	 */
	private void createNode(Wall wall, Array<Wall> walls) {
		// Create new node
		Node wallNode = new Node();
		wallNode.id = wallId(wall);

		// Add node to model
		builder.getHouseModel().getNode(NODE_WALLS).addChild(wallNode);

		createFaces(wallNode, wall, walls);
		createTop(wallNode, walls);
	}

	/**
	 * Create node parts for sides
	 * @param node Node to insert node parts
	 * @param wall Wall to insert in node
	 * @param walls All walls
	 */
	private void createFaces(Node node, Wall wall, Array<Wall> walls) {
		// Get faces
		Array<WallFace> faces = getWallFaces(wall, walls);

		// Create node part for each face and add it
		for(WallFace face : faces) {
			node.parts.add(new NodePart(face.meshPart, face.material));
		}
	}

	/**
	 * Create node parts for top of all walls in the floor
	 * @param node Node to insert node part
	 * @param walls All walls
	 */
	private void createTop(Node node, Array<Wall> walls) {
		// Create meshpart
		MeshPart part = builder.getBuilder().part("walls_top", GL20.GL_TRIANGLES);

		// Loop through all walls
		for(int i = 0; i < walls.size; i++) {
			// Get wall
			Wall wall = walls.get(i);

			// Compute 3d points
			Vector3[] points = get3dVectors(wall, walls);

			// Create vertices
			builder.getBuilder().rect(
				new VertexInfo().set(points[4], null, null, null),
				new VertexInfo().set(points[5], null, null, null),
				new VertexInfo().set(points[7], null, null, null),
				new VertexInfo().set(points[6], null, null, null)
				);
		}

		// Add meshPart in node
		node.parts.add(new NodePart(part, new Material(ColorAttribute.createDiffuse(Color.BLACK))));
	}

	/**
	 * Return an array of faces
	 * @param wall Wall to create
	 * @param walls All walls
	 * @return array of faces
	*/
	private Array<WallFace> getWallFaces(Wall wall, Array<Wall> walls) {
		// Init faces array
		Array<WallFace> faces = new Array<WallFace>();

		// Compute 3d points
		Vector3[] points = get3dVectors(wall, walls);

		// Compute linked points
		boolean p0linked = computer.isPointLinked(wall, 0, walls);
		boolean p1linked = computer.isPointLinked(wall, 1, walls);

		// Create side faces for p0 and p1 if not linked
		MeshPart part = null;
		String wallId = wallId(wall);

		if(!p0linked) {
			part = builder.getBuilder().part(wallId + "0", GL20.GL_TRIANGLES);
			builder.getBuilder().rect(
				new VertexInfo().set(points[0], null, null, null),
				new VertexInfo().set(points[1], null, null, null),
				new VertexInfo().set(points[5], null, null, null),
				new VertexInfo().set(points[4], null, null, null)
				);

			faces.add(new WallFace(part, new Material(ColorAttribute.createDiffuse(Color.RED))));
		}

		if(!p1linked) {
			part = builder.getBuilder().part(wallId + "1", GL20.GL_TRIANGLES);
			builder.getBuilder().rect(
				new VertexInfo().set(points[2], null, null, null),
				new VertexInfo().set(points[6], null, null, null),
				new VertexInfo().set(points[7], null, null, null),
				new VertexInfo().set(points[3], null, null, null)
				);

			faces.add(new WallFace(part, new Material(ColorAttribute.createDiffuse(Color.YELLOW))));
		}

		// Create sides

		// Left side
		part = builder.getBuilder().part(wallId + "2", GL20.GL_TRIANGLES);
		builder.getBuilder().rect(
			new VertexInfo().set(points[0], null, null, null),
			new VertexInfo().set(points[4], null, null, null),
			new VertexInfo().set(points[6], null, null, null),
			new VertexInfo().set(points[2], null, null, null)
		);

		faces.add(new WallFace(part, new Material(ColorAttribute.createDiffuse(Color.GREEN))));

		// Right side
		part = builder.getBuilder().part(wallId + "3", GL20.GL_TRIANGLES);
		builder.getBuilder().rect(
			new VertexInfo().set(points[1], null, null, null),
			new VertexInfo().set(points[3], null, null, null),
			new VertexInfo().set(points[7], null, null, null),
			new VertexInfo().set(points[5], null, null, null)
		);

		faces.add(new WallFace(part, new Material(ColorAttribute.createDiffuse(Color.BLUE))));

		return faces;
	}

	/** Compute 3d points of wall
	 * @param wall Wall to compute
	 * @param walls All floor's wall
	 * @return array of Vector3
	*/
	private Vector3[] get3dVectors(Wall wall, Array<Wall> walls) {
		// Compute result points
		Point[] resultPoints = new Point[4];
		for(int i = 0; i < 4; i++) resultPoints[i] = new Point();

		computer.extrudeWall(wall, walls, resultPoints);

		// Compute bottom and top points
		Vector3[] points = new Vector3[8];

		for(int i = 0; i < resultPoints.length; i++) {
			Point p = resultPoints[i];

			points[i] = new Vector3().set(p.x, p.y, 0); // Bottom
			points[i+4] = new Vector3().set(p.x, p.y, wall.getHeight()); // Top
		}

		return points;
	}
}