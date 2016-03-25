uniform mat4 u_projViewTrans;
uniform mat4 u_worldTrans;
uniform mat3 u_normalMatrix;
uniform vec4 u_uvTransform;
uniform float u_cameraNear;
uniform float u_cameraFar;

in vec3 a_position;
in vec2 a_texCoord;
in vec3 a_normal;
in vec3 a_tangent;
in vec3 a_binormal;

out vec3 v_normal;
out vec3 v_tangent;
out vec3 v_binormal;
out vec2 v_uv;
out float v_depth;

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
	v_normal = normalize(u_normalMatrix * a_normal);
	v_tangent = normalize(u_normalMatrix * a_tangent);
	v_binormal = normalize(u_normalMatrix * a_binormal);

	vec4 pos = u_worldTrans * vec4(a_position, 1.0);

	// Compute linear z
	v_depth = (pos.z - u_cameraNear) / (u_cameraFar - u_cameraNear);

	gl_Position = u_projViewTrans * pos;
}
