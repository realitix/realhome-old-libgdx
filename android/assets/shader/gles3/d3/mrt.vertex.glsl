uniform mat4 u_projViewTrans;
uniform mat4 u_worldTrans;
uniform vec4 u_diffuseColor;

in vec3 a_position;

out vec4 v_color;


void main() {
	v_color = u_diffuseColor;

	vec4 pos = u_worldTrans * vec4(a_position, 1.0);

	gl_Position = u_projViewTrans * pos;
}
