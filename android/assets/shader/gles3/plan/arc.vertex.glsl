in vec2 a_position;

uniform mat4 u_projViewTrans;

out vec2 v_pos;

void main() {
	vec4 pos = vec4(a_position, 0.0, 1.0);
	v_pos = pos.xy;
	gl_Position = u_projViewTrans * pos;
}