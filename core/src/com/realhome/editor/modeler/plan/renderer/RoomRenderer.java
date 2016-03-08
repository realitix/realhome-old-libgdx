package com.realhome.editor.modeler.plan.renderer;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.modeler.plan.PlanConfiguration;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.RoomPlan;
import com.realhome.editor.util.RealShader;

public class RoomRenderer implements Renderer {
	private Mesh mesh;
	private Array<RoomPlan> rooms;
	private int id;

	// Shader
	private ShaderProgram shader;

	private float[] vertices;

	public RoomRenderer() {
		initShader();
		initMesh();
	}

	@Override
	public void init(HousePlan housePlan) {
		rooms = housePlan.getRooms();
	}

	private void initShader() {
		shader = RealShader.create("plan/grid");
	}

	private void initMesh() {
		int maxVertices = 2000; // 2 points per line
		mesh = new Mesh(true, maxVertices, 0, new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE));
		vertices = new float[maxVertices * (mesh.getVertexAttributes().vertexSize / 4)];
	}

	public void update() {
		id = 0;
		for (RoomPlan room : rooms) {
			for(int i = 1; i < room.getPoints().size; i++) {
				line(room.getPoints().get(i-1), room.getPoints().get(i));
			}

			// Last with first
			line(room.getPoints().get(0), room.getPoints().get(room.getPoints().size-1));
		}

		mesh.setVertices(vertices, 0 , id);
	}

	private void line(Point p1, Point p2) {
		vertices[id++] = p1.x;
		vertices[id++] = p1.y;
		vertices[id++] = p2.x;
		vertices[id++] = p2.y;
	}

	@Override
	public void render(OrthographicCamera camera) {
		update();

		shader.begin();
		shader.setUniformMatrix("u_projViewTrans", camera.combined);
		shader.setUniformf("u_color", PlanConfiguration.Room.color);
		mesh.render(shader, GL20.GL_LINES);
		shader.end();
	}

	@Override
	public void dispose () {
		shader.dispose();
		mesh.dispose();
	}
}