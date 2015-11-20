varying vec2 v_normal;
varying vec4 v_color;

// Between 0..1 When we start antialias
//uniform u_feather;

void main() {
	float feather = 0.5;
	float distance = length(v_normal);
	float alpha = 1.0;

	//if(distance >= feather) {
	//	alpha = (distance + feather) * feather;
	//}
	
	vec4 color = v_color;
	color.a = 1.0 - distance;
	gl_FragColor = color;
}