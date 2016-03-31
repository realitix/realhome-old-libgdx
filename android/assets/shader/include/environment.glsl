#ifdef lightingFlag

#if numDirectionalLights > 0
	struct DirectionalLight {
		vec3 color;
		vec3 direction;
	};

	uniform DirectionalLight u_dirLights[numDirectionalLights];
#endif

#if numPointLights > 0
	struct PointLight {
		vec3 color;
		vec3 position;
		float intensity;
	};

	uniform PointLight u_pointLights[numPointLights];
#endif

#if numSpotLights > 0
	struct SpotLight {
		vec3 color;
		vec3 position;
		vec3 direction;
		float intensity;
		float cutoffAngle;
		float exponent;
	};

	uniform SpotLight u_spotLights[numSpotLights];
#endif

#endif