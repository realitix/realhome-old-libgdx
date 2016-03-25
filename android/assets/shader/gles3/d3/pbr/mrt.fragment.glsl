/**
 * u_textures contains all our textures
 * 0 = albedo (rgb)
 * 1 = normal (rgb)
 * 2 = roughness (a)
 * 3 = metalness (a)
*/
uniform sampler2DArray u_textures;
uniform float u_cameraFar;

in vec3 v_normal;
in vec3 v_tangent;
in vec3 v_binormal;
in vec2 v_uv;
in float v_depth;

layout(location = 0) out vec4 gbuffer0;
layout(location = 1) out vec4 gbuffer1;

#include depth_functions

void main() {
	// Fetch parameters
	vec3 albedo = texture(u_textures, vec3(v_uv, 0.0)).rgb;
	vec3 normal = texture(u_textures, vec3(v_uv, 1.0)).rgb;

	// Compute normal in world space
	mat3 tbn = mat3(v_tangent, v_binormal, v_normal);
	normal = normalize(2.0 * texture(u_textures, vec3(v_uv, 1.0)).rgb - 1.0) * tbn;


	// Fill the g-buffers
	gbuffer0 = vec4(texture(u_textures, vec3(v_uv, 3.0)).a, 0.0, 0.0, 1.0);
	gbuffer1 = vec4(normal, 1.0);

	/**
	 * Overwrite FragDepth in fragment shader
	 * Warning! Performance cost!
	 * We use linear depth distribution (not logarythmic)
	 *
	 * Performance comparison: https://mynameismjp.wordpress.com/2010/03/22/attack-of-the-depth-buffer/
	 * Log depth: http://outerra.blogspot.fr/2013/07/logarithmic-depth-buffer-optimizations.html
	*/
	gl_FragDepth = v_depth;
}
