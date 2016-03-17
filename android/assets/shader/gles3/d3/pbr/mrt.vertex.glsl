uniform mat4 u_projViewTrans;
uniform mat4 u_worldTrans;
uniform vec4 u_diffuseColor;
uniform mat3 u_normalMatrix;

in vec3 a_position;
in vec3 a_normal;

out vec3 v_normal;
out vec4 v_color;

void main() {
	v_color = u_diffuseColor;
	v_normal = normalize(u_normalMatrix * a_normal);

	vec4 pos = u_worldTrans * vec4(a_position, 1.0);

	gl_Position = u_projViewTrans * pos;
}
