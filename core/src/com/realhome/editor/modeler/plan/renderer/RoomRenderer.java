package com.realhome.editor.modeler.plan.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.RoomPlan;
import com.realhome.editor.util.RealShader;

public class RoomRenderer implements Renderer {
	private Mesh mesh;
	private Array<RoomPlan> rooms;

	// Shader
	private ShaderProgram shader;

	private float[] vertices;

	public RoomRenderer() {
		initShader();
		initMesh();
	}

	@Override
	public void init(HousePlan housePlan) {
		rooms = housePlan.rooms;
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
		int id = 0;
		for (RoomPlan room : rooms) {
			for(int i = 1; i < room.points.size; i++) {
				vertices[id++] = room.points.get(i-1).x;
				vertices[id++] = room.points.get(i-1).y;

				vertices[id++] = room.points.get(i).x;
				vertices[id++] = room.points.get(i).y;
			}

			// Last with first
			vertices[id++] = room.points.get(0).x;
			vertices[id++] = room.points.get(0).y;
			vertices[id++] = room.points.get(room.points.size-1).x;
			vertices[id++] = room.points.get(room.points.size-1).y;
		}

		mesh.setVertices(vertices, 0 , id);
	}

	@Override
	public void render(OrthographicCamera camera) {
		update();

		shader.begin();
		shader.setUniformMatrix("u_projViewTrans", camera.combined);

		/*Random rand = new Random();
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();*/
		shader.setUniformf("u_color", Color.RED);
		mesh.render(shader, GL20.GL_LINES);
		shader.end();
	}

	@Override
	public void dispose () {
		shader.dispose();
		mesh.dispose();
	}
}