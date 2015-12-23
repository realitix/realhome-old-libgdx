attribute vec2 a_position;

uniform mat4 u_projViewTrans;


void main() {
	vec4 pos = vec4(a_position, 0.0, 1.0);
	gl_Position = u_projViewTrans * pos;
}