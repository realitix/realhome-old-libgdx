uniform float u_lineWidth;

varying vec2 v_normal;
varying vec4 v_color;

void main() {
	float feather = 0.5;
	float dist = length(v_normal) / u_lineWidth;

	gl_FragColor = v_color * (1.0 - dist);
}