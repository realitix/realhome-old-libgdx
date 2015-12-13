uniform vec4 u_color;
uniform vec2 u_tile;
uniform vec4 u_colorBack;
uniform vec4 u_colorFront;
uniform float u_lineWidth;

varying vec2 v_uv;

// http://stackoverflow.com/questions/10301001/perpendicular-on-a-line-segment-from-a-given-point
float getDistanceFromCenter(vec2 point) {
    const float x1 = 0.0;
    const float y1 = 0.0;
    const float x2 = 1.0;
    const float y2 = 1.0;

    float x3 = point.x;
    float y3 = point.y;

    const float px = x2 - x1;
    const float py = y2 - y1;
    const float dAB = px*px + py*py;

    float u = ((x3 - x1) * px + (y3 - y1) * py) / dAB;

    float x = x1 + u * px;
    float y = y1 + u * py;

    return distance(vec2(x, y), point);
}

void main() {
	const float distanceCenterToCoin = 0.70711;
	
	vec2 phase = fract(v_uv / u_tile);
	float distanceFromCenter = getDistanceFromCenter(phase);

	vec4 background = u_colorBack;
	vec4 foreground = vec4(0.0, 0.0, 0.0, 0.0);

	if(distanceFromCenter <= u_lineWidth) {
		foreground = u_colorFront;
		foreground.a *= 1.0 - distanceFromCenter / u_lineWidth;
	}
	else if (distanceFromCenter >= distanceCenterToCoin - u_lineWidth) {
		foreground = u_colorFront;
		float distanceFromCoin = distanceCenterToCoin - distanceFromCenter;
		foreground.a *= 1.0 - distanceFromCoin / u_lineWidth;
	}

	gl_FragColor = mix(background, foreground, foreground.a);
}