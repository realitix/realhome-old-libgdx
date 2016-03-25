uniform sampler2D u_gbuffer0;
uniform sampler2D u_gbuffer1;
uniform sampler2D u_gbuffer2;
uniform float u_cameraFar;
uniform float u_cameraPosition;

#include environment
#include depth_functions
#include pbr_functions

in vec2 v_uv;
in vec3 v_viewRay;

out vec4 fragmentColor;

void main() {
	float depth = texture(u_gbuffer2, v_uv).r;

	vec3 pos = getPositionFromDepth(depth, v_viewRay, u_cameraPosition);

	fragmentColor = vec4(depth, 0.0, 0.0, 1.0);

}
