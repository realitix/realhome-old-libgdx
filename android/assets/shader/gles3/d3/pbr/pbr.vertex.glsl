/*
 * Frustum corners of far plane in world space
 * Used to compute the view ray needed to reconstruct
 * position from depth.
*/
uniform vec3 u_frustumCorners[4];
uniform vec3 u_cameraPosition;

in vec2 a_position;
in float a_cornerIndex;

out vec2 v_uv;
out vec3 v_frustumRay;

#include depth_functions

void main() {
	v_uv.xy = a_position.xy * 0.5 + 0.5;
	v_frustumRay = getViewRay(u_frustumCorners[int(a_cornerIndex)], u_cameraPosition);
	gl_Position = vec4(a_position.x, a_position.y, 0.0, 1.0);
}
