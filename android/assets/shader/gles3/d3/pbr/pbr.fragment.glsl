/**
 * g_buffers configuration
 * 0 = albedo (rgb) metallic (a)
 * 1 = normal (rgb)
 * 2 = roughness (r)
 * 3 = depth (a)
*/

uniform sampler2D u_gbuffer0;
uniform sampler2D u_gbuffer1;
uniform sampler2D u_gbuffer2;
uniform sampler2D u_gbuffer3;

uniform vec3 u_cameraPosition;
uniform float u_cameraNear;
uniform float u_cameraFar;

in vec2 v_uv;
in vec3 v_frustumRay;

out vec4 fragmentColor;

#include environment
#include depth_functions
#include pbr_functions

void main() {
	// Albedo
	vec3 albedo = texture(u_gbuffer0, v_uv).rgb;
	// Normal in world space
	vec3 normal = texture(u_gbuffer1, v_uv).rgb;
	// Roughness
	float roughness = texture(u_gbuffer2, v_uv).r;
	// Metallic
	float metallic = texture(u_gbuffer0, v_uv).a;
	// Linear depth from 0 to 1
	float depth = texture(u_gbuffer3, v_uv).r;
	// Position of pixel in world space
	vec3 position = getPositionFromDepth(depth, v_frustumRay, u_cameraPosition, u_cameraNear, u_cameraFar);
	// Direction from camera to pixel in world space
	vec3 viewDir = normalize(u_cameraPosition - position);
	// Computed light
	vec3 finalLight = vec3(0.0);

	#ifdef lightingFlag
		#if numDirectionalLights > 0
			/*for (int i = 0; i < numDirectionalLights; i++) {
				vec3 lightDir = -u_dirLights[i].direction;
				vec3 lightContribution = computeLightContribution(normal, lightDir, viewDir, albedo, u_dirLights[i].color, metallic, roughness);
				finalLight += lightContribution;
			}*/

			vec3 lightDir = normalize(-u_dirLights[0].direction);
			vec3 lightContribution = computeLightContribution(normal, lightDir, viewDir, albedo, u_dirLights[0].color, metallic, roughness);
			finalLight += normal;
		#endif

		#if numPointLights > 0
			/*for (int i = 0; i < numPointLights; i++) {
				vec3 lightToPosition = u_pointLights[i].position - position;
				vec3 lightDir = normalize(lightToPosition);
				vec3 lightContribution = computeLightContribution(normal, lightDir, viewDir, albedo, u_pointLights[i].color, metallic, roughness);

				float squareDistance = pow(length(lightToPosition), 2.0);
				float falloff = u_pointLights[i].intensity / (1.0 + squareDistance);

				finalLight += lightContribution * falloff;
			}*/
		#endif

		#if numSpotLights > 0
			/*for (int i = 0; i < numSpotLights; i++) {
				vec3 lightToPosition = u_spotLights[i].position - position;
				vec3 lightDir = normalize(lightToPosition);


				float spotEffect = dot(-lightDir, u_spotLights[i].direction);
				if ( spotEffect > cos(radians(u_spotLights[i].cutoffAngle)) ) {
					spotEffect = pow(max(spotEffect, 0.0), u_spotLights[i].exponent);

					vec3 lightContribution = computeLightContribution(normal, lightDir, viewDir, albedo, u_spotLights[i].color, metallic, roughness);

					float squareDistance = pow(length(lightToPosition), 2.0);
					float falloff = u_spotLights[i].intensity / (1.0 + squareDistance);

					finalLight += lightContribution * falloff * spotEffect;
				}
			}*/
		#endif

	#endif

	fragmentColor = vec4(finalLight, 1.0);
}
