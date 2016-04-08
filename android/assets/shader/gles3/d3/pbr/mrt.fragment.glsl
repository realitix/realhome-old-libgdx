/**
 * u_textures contains all our textures
 * 0 = albedo (rgb)
 * 1 = normal (rgb)
 * 2 = roughness (a)
 * 3 = metalness (a)
*/
uniform sampler2DArray u_textures;
uniform float u_cameraFar;

in mat3 v_tbn;
in vec2 v_uv;
in float v_depth;
in vec3 v_tangentCameraPosition;
in vec3 v_tangentPosition;

layout(location = 0) out vec4 gbuffer0;
layout(location = 1) out vec4 gbuffer1;
layout(location = 2) out vec4 gbuffer2;

#include depth_functions
#include parallax

void main() {
	vec3 viewDir = normalize(v_tangentCameraPosition - v_tangentPosition);
	float heightScale = 1;
	vec2 uv = parallaxMapping(v_uv, viewDir, u_textures, heightScale);

	// Fetch parameters
	vec3 albedo = texture(u_textures, vec3(uv, 0.0)).rgb;
	vec3 normal = texture(u_textures, vec3(uv, 1.0)).rgb;
	float roughness = texture(u_textures, vec3(uv, 2.0)).a;
	float metallic = texture(u_textures, vec3(uv, 3.0)).a;

	// Compute normal in world space
	normal = v_tbn * normalize(2.0 * normal - 1.0); // Pass in world space
	normal = 0.5 * normal + 0.5; // Clamp to 0,1. Don't Normalize!

	// Fill the g-buffers
	gbuffer0 = vec4(albedo, metallic);
	gbuffer1 = vec4(normal, 1.0);
	gbuffer2 = vec4(roughness, 0.0, 0.0, 0.0);

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
