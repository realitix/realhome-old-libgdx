uniform mat4 u_projViewTrans;

attribute vec2 a_position;
attribute vec2 a_uv;

varying vec2 v_uv;

void main() {
	v_uv = a_uv;
	
	gl_Position = u_projViewTrans * vec4(a_position, 0.0, 1.0);
}