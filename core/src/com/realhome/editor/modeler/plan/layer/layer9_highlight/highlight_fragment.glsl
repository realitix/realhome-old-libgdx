uniform vec2 u_p1;
uniform vec2 u_p2;
uniform vec2 u_min;
uniform vec2 u_max;
uniform float u_circleSize;
uniform float u_lineSize;
uniform vec4 u_circleColor;
uniform vec4 u_lineColor;

varying vec2 v_pos;

bool inLine() {
	if( v_pos.x >= u_min.x - u_lineSize &&
		v_pos.x <= u_max.x + u_lineSize &&
		v_pos.y >= u_min.y - u_lineSize &&
		v_pos.y <= u_max.y + u_lineSize) {
		return true;
	}

	return false;
}

float getDistanceFromCenter(vec2 point, vec2 a, vec2 b) {
    float x1 = a.x;
    float y1 = a.y;
    float x2 = b.x;
    float y2 = b.y;

    float x3 = point.x;
    float y3 = point.y;

    float px = x2 - x1;
    float py = y2 - y1;
    float dAB = px*px + py*py;

    float u = ((x3 - x1) * px + (y3 - y1) * py) / dAB;

    float x = x1 + u * px;
    float y = y1 + u * py;

    return distance(vec2(x, y), point);
}



void main() {
	vec4 color = vec4(0.0);
		
	// Circle
	if(distance(v_pos, u_p1) <= u_circleSize || distance(v_pos, u_p2) <= u_circleSize) {
		color = u_circleColor;
	}
	// Line
	else if(inLine()){
		if(getDistanceFromCenter(v_pos, u_p1, u_p2) <= u_lineSize) {
			color = u_lineColor;
		}
	}

	gl_FragColor = color;
}