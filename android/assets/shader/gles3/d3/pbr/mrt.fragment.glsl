/**
 * u_textures contains all our textures
 * 0 = normal
*/
uniform sampler2DArray u_textures;

in vec3 v_normal;
in vec3 v_tangent;
in vec3 v_binormal;
in vec2 v_uv;

layout(location = 0) out vec4 gbuffer0;
layout(location = 1) out vec4 gbuffer1;


void main() {
	// Compute normal in world space
	mat3 tbn = mat3(v_tangent, v_binormal, v_normal);
	vec3 normal = normalize(2.0 * texture(u_textures, vec3(v_uv, 0.0)).xyz - 1.0) * tbn;

	gbuffer1 = vec4(normal, 1.0);
}
