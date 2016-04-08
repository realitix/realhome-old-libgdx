uniform mat4 u_projTrans;
uniform mat4 u_viewTrans;
uniform mat4 u_worldTrans;
uniform mat3 u_normalMatrix;
uniform vec4 u_uvTransform;
uniform vec3 u_cameraPosition;
uniform float u_cameraNear;
uniform float u_cameraFar;

in vec3 a_position;
in vec2 a_texCoord;
in vec3 a_normal;
in vec3 a_tangent;
in vec3 a_binormal;

out mat3 v_tbn;
out vec2 v_uv;
out float v_depth;
out vec3 v_tangentCameraPosition;
out vec3 v_tangentPosition;

void main() {
	//v_uv = u_uvTransform.xy + a_texCoord * u_uvTransform.zw;
	v_uv = a_texCoord;

	/*
	 * We must multiply normal with normalMatrix because
	 * worldTrans matrix doesn't preserver normal if scaled.
	 * Worldtrans preserves tangent but we apply normalMatrix
	 * to stay consistent. I don't know for binormal so I apply
	 * normalMatrix too.
	 * see http://www.lighthouse3d.com/tutorials/glsl-12-tutorial/the-normal-matrix/
	*/
	v_tbn = mat3(
		normalize(u_normalMatrix * a_tangent),
		normalize(u_normalMatrix * a_binormal),
		normalize(u_normalMatrix * a_normal)
	);

	vec4 worldPos = u_worldTrans * vec4(a_position, 1.0);
	vec4 viewPos = u_viewTrans * worldPos;

	// Compute linear z
	// We take -z because it's the view matrix
	// http://www.codinglabs.net/article_world_view_projection_matrix.aspx
	v_depth = (-viewPos.z - u_cameraNear) / (u_cameraFar - u_cameraNear);

	mat3 tbnInversed = transpose(v_tbn);
	v_tangentCameraPosition = tbnInversed * u_cameraPosition;
	v_tangentPosition = tbnInversed * worldPos.xyz;

	gl_Position = u_projTrans * viewPos;
}
