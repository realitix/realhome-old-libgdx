/**
 * u_textures contains all our textures
 * 0 = albedo (rgb)
 * 1 = normal (rgb)
 * 2 = roughness (a)
 * 3 = metalness (a)
 * 4 = displacement (a)
*/
uniform sampler2D u_albedoTexture;
uniform sampler2D u_normalTexture;
uniform sampler2D u_roughnessTexture;
uniform sampler2D u_metalnessTexture;
uniform sampler2D u_displacementTexture;

uniform float u_cameraFar;
uniform float u_displacementScale;

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
	vec3 tangentViewDir = normalize(v_tangentCameraPosition - v_tangentPosition);
	//vec2 uv = parallaxMapping(v_uv, tangentViewDir, u_displacementScale);
	vec2 uv = v_uv;

	// Fetch parameters
	vec3 albedo = texture(u_albedoTexture, uv).rgb;
	vec3 normal = texture(u_normalTexture, uv).rgb;
	float roughness = texture(u_roughnessTexture, uv).r;
	float metallic = texture(u_metalnessTexture, uv).r;

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
