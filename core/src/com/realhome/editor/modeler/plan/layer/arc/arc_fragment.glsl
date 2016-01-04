uniform vec2 u_pos;
uniform vec2 u_posLeft;
uniform vec2 u_posRight;

uniform float u_size;
uniform vec4 u_color;
uniform vec4 u_outlineColor;
uniform float u_outlineSize;

varying vec2 v_pos;

float getDistanceFromLine(vec2 point, vec2 a, vec2 b) {
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

bool isOutline(float left, float right, float distance) {
	if(left <= u_outlineSize && distance <= u_size) {
		return true;
	}
	else if(right <= u_outlineSize && distance <= u_size) {
		return true;
	}
	else if( distance > u_size && distance <= u_size + u_outlineSize) {
		return true;
	}
	
	return false;
}

bool isPosOutline(float distance) {
	float left = getDistanceFromLine(v_pos, u_pos, u_posLeft);
	float right = getDistanceFromLine(v_pos, u_pos, u_posRight);
	
	return isOutline(left, right, distance);
}

void main() {
	vec4 color = vec4(0.0, 0.0, 0.0, 0.0);
	
	float distance_line_left = getDistanceFromLine(v_pos, u_pos, u_posLeft);
	float distance_line_right = getDistanceFromLine(v_pos, u_pos, u_posRight);
	
	float d = distance(v_pos, u_pos);

	if(isPosOutline(d)) {
		color = u_outlineColor;
	}
	else if(d <= u_size) {
		color = u_color;
	}

	gl_FragColor = color;
}