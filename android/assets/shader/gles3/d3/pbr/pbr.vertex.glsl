in vec2 a_position;
// Frustum corner of far plane
in vec2 a_frustumCorner;

out vec2 v_uv;
out vec3 v_viewRay;

/* View vector (vertex to light) */
out vec3 v_L;

void main() {
	v_uv.xy = a_position.xy * 0.5 + 0.5;
	v_viewRay = getViewRay(a_frustumCorner, u_viewTrans);
	gl_Position = vec4(a_position.x, a_position.y, 0.0, 1.0);
}
