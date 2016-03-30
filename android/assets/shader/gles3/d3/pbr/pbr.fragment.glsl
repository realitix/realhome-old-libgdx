/**
 * g_buffers configuration
 * 0 = albedo (rgb)
 * 1 = normal (rgb)
 * 2 = depth (a)
*/

uniform sampler2D u_gbuffer0;
uniform sampler2D u_gbuffer1;
uniform sampler2D u_gbuffer2;

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
	vec3 albedo = texture(u_gbuffer0).rgb;
	// Normal in view space
	vec3 normal = texture(u_gbuffer1).rgb;
	// Roughness
	float roughness = texture(u_gbuffer3).r;
	// Metallic
	float metallic = texture(u_gbuffer4).r;
	// Linear depth from 0 to 1
	float depth = texture(u_gbuffer2, v_uv).r;
	// Position of pixel in world space
	vec3 position = getPositionFromDepth(depth, v_frustumRay, u_cameraPosition, u_cameraNear, u_cameraFar);
	// Direction from camera to pixel in world space
	vec3 viewDir = normalize(u_cameraPosition - position);

	#ifdef lightingFlag
		#if numDirectionalLights > 0
			for (int i = 0; i < numDirectionalLights; i++) {
				vec3 lightDir = -u_dirLights[i].direction;
				vec3 lightContribution = computeLightContribution(normal, lightDir, viewDir, albedo, u_dirLights[i].color, metallic, roughness);
				lightContribution *= u_dirLights[i].intensity;
			}
		#endif

		#if numPointLights > 0
			for (int i = 0; i < numPointLights; i++) {
				vec3 lightToPosition = u_pointLights[i].position - position;
				vec3 lightDir = normalize(lightToPosition);
				vec3 lightContribution = computeLightContribution(normal, lightDir, viewDir, albedo, u_pointLights[i].color, metallic, roughness);

				float squareDistance = pow(length(lightToPosition), 2.0);
				float falloff = u_pointLights[i].intensity / (1.0 + squareDistance);

				lightContribution *= falloff;
			}
		#endif

		#if numSpotLights > 0
			for (int i = 0; i < numSpotLights; i++) {
				vec3 lightToPosition = u_spotLights[i].position - position;
				vec3 lightDir = normalize(lightToPosition);


				float spotEffect = dot(-lightDir, u_spotLights[i].direction);
				if ( spotEffect > cos(radians(u_spotLights[i].cutoffAngle)) ) {
					vec3 lightContribution = computeLightContribution(normal, lightDir, viewDir, albedo, u_spotLights[i].color, metallic, roughness);
					spotEffect = max( pow( max( spotEffect, 0.0 ), u_spotLights[i].exponent ), 0.0 );

					float squareDistance = pow(length(lightToPosition), 2.0);
					float falloff = u_spotLights[i].intensity / (1.0 + squareDistance);

					lightContribution *= falloff;
				}
			}
		#endif

	#endif

	fragmentColor = vec4(col, 1.0);

}
