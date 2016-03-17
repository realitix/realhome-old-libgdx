package com.realhome.editor.modeler.d3.builder;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;
import com.realhome.editor.model.house.Floor;
import com.realhome.editor.modeler.util.RoomComputer;
import com.realhome.editor.util.math.PolygonUtils;

public class RoomBuilder {
	public final static String NODE_ROOMS = "nodeRooms";

	private HouseModelBuilder builder;
	private RoomComputer computer;
	private final EarClippingTriangulator triangulator;

	public RoomBuilder(HouseModelBuilder builder) {
		this.builder = builder;
		computer = new RoomComputer();
		triangulator = new EarClippingTriangulator();
	}

	public void sync() {
		// Check rooms Node
		if(builder.getHouseModel().getNode(NODE_ROOMS) == null) {
			Node roomsNode = new Node();
			roomsNode.id = NODE_ROOMS;
			builder.getHouseModel().nodes.add(roomsNode);
		}

		// Compute rooms
		for(Floor floor : builder.getHouse().getFloors()) {
			Array<Array<Vector2>> rooms = computer.getRooms(floor.getWalls());
			for(Array<Vector2> room : rooms) {
				createNode(room);
			}
		}
	}

	/** Create node for room in parameter
	 * @param room Room to create
	 */
	private void createNode(Array<Vector2> room) {
		// Create new node
		Node roomNode = new Node();
		roomNode.id = "test";

		// Add node to model
		builder.getHouseModel().getNode(NODE_ROOMS).addChild(roomNode);

		createRoom(roomNode, room);
	}

	/**
	 * Create node parts for room
	 * @param node Node to insert node parts
	 * @param room Room to insert in node
	 */
	private void createRoom(Node node, Array<Vector2> room) {
		// Create meshpart
		MeshPart part = builder.getBuilder().part("room_test", GL20.GL_TRIANGLES);

		// Init triangles
		FloatArray floatPoints = new FloatArray();
		ShortArray triangles = null;

		// Init normal
		Vector3 normal = new Vector3(0, 0, 1);

		// Add points in FloatArray
		for(int i = 0; i < room.size; i++) {
			floatPoints.add(room.get(i).x);
			floatPoints.add(room.get(i).y);
		}

		// Triangulate
		triangles = triangulator.computeTriangles(floatPoints);

		// Compute vertices
		Material material = new Material(ColorAttribute.createDiffuse(Color.GREEN));
		for (int i = 0; i < triangles.size; i += 3) {
			int p1 = triangles.get(i) * 2;
			int p2 = triangles.get(i + 1) * 2;
			int p3 = triangles.get(i + 2) * 2;

			Array<Vector2> triangle = new Array<Vector2>();
			triangle.add(new Vector2(floatPoints.get(p1), floatPoints.get(p1 + 1)));
			triangle.add(new Vector2(floatPoints.get(p3), floatPoints.get(p3 + 1)));
			triangle.add(new Vector2(floatPoints.get(p2), floatPoints.get(p2 + 1)));

			if(!PolygonUtils.isClockwise(triangle)) {
				material = new Material(ColorAttribute.createDiffuse(Color.RED));
				triangle.reverse();
			}


			builder.getBuilder().triangle(
				new VertexInfo().setPos(triangle.get(0).x, triangle.get(0).y, 0).setNor(normal),
				new VertexInfo().setPos(triangle.get(1).x, triangle.get(1).y, 0).setNor(normal),
				new VertexInfo().setPos(triangle.get(2).x, triangle.get(2).y, 0).setNor(normal)
				);
		}

		// Add meshPart in node
		node.parts.add(new NodePart(part, material));
	}
}