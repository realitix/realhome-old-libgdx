attribute vec2 a_position;
attribute vec2 a_normal;
attribute vec4 a_color;

uniform mat4 u_worldTrans;
uniform mat4 u_projViewTrans;
uniform float u_lineWidth;

varying vec2 v_normal;
varying vec4 v_color;

void main() {
	v_color = a_color;
	v_normal = a_normal;

	vec2 delta = a_normal * u_lineWidth;
	vec4 pos = u_worldTrans * vec4(a_position, 0.0, 1.0);
	pos.xy += delta;

	gl_Position = u_projViewTrans * pos;
}