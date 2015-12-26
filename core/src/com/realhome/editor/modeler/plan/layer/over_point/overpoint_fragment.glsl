uniform vec2 u_point;
uniform vec4 u_color;
uniform float u_circleSize;
uniform float u_borderSize;

varying vec2 v_pos;

void main() {

	vec4 color = vec4(0.0, 0.0, 0.0, 0.0);
	
	float d = distance(v_pos, u_point);
	
	if( d <= u_circleSize || ( d >= (u_circleSize * 2.0) && d <= (u_circleSize * 2.0 + u_borderSize)) ) {
		color = u_color;
	}

	gl_FragColor = color;
}