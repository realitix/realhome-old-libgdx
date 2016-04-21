/**
 * To fully understand theses functions,
 * see http://www.codinglabs.net/article_physically_based_rendering_cook_torrance.aspx
*/

const float Pi = 3.1415926535897932384626433832795;

/* GGX/Trowbridge-Reitz normal distribution function
 * @param NdotH Normal . HalfVector
 * @param alpha Roughness
 * @return percent of orientation of the micro facets (between 0,1)
*/
float distributionGGX(float NdotH, float alpha) {
	float alpha2 = alpha * alpha;
	float d = NdotH * NdotH * (alpha2 - 1.0) + 1.0;
	return alpha2 / (Pi * d * d);
}

/* Sub function of geometryGGX */
float subGeometryGGX(float Ndot, float alpha) {
	float d = (1.0 - Ndot * Ndot) / (Ndot * Ndot);
	return 2.0 / (1.0 + sqrt(1.0 + alpha * alpha * d));
}

/* GGX/Trowbridge-Reitz geometry function
 * Smith model
 * @param NdotL Normal .
 * @param NdotV Normal .
 * @param alpha Roughness
 * @return self shadowing of the micro facets (between 0,1)
*/
float geometryGGX(float NdotL, float NdotV, float alpha) {
	return subGeometryGGX(NdotL, alpha) * subGeometryGGX(NdotV, alpha);
}

/* GGX/Fresnel function
 * Schlick’s approximation with Spherical Gaussian approximation
 * see http://blog.selfshadow.com/publications/s2013-shading-course/karis/s2013_pbs_epic_notes_v2.pdf
 * @param NdotH Normal . HalfVector
 * @param F0 Material's response at normal incidence
*/
vec3 fresnelGGX(float VdotH, vec3 F0) {
	const float a = -5.55473;
	const float b = -6.98316;
	return F0 + (1.0 - F0) * pow(2, (a * VdotH - b) * VdotH);
}

/*
 * FO computation function
 * albedo map contains metallic color if metallic = 1
 * if metallic = 0, we use the standard (4% for dielectric)
 * @param albedo Albedo map
 * @param metallic 0 = dielectric, 1 = metallic
*/
vec3 computeF0(vec3 albedo, float metallic) {
	const vec3 dielectric = vec3(0.03, 0.03, 0.03);
	return mix(dielectric, albedo, metallic);
}

/* Cook-Torrance specular computation
*/
vec3 cookTorrance(vec3 albedo, float NdotH, float NdotL,
				  float NdotV, float VdotH, float alpha,
				  float metallic) {
	float d = distributionGGX(NdotH, alpha);
	vec3 f = fresnelGGX(VdotH, computeF0(albedo, metallic));
	float g = geometryGGX(NdotL, NdotV, alpha);
	float denominator = 4.0 * NdotL * NdotV;

	return d * f * g / denominator;
}

/**
 * Compute diffuse but keep things physically good
*/
vec3 conservativeDiffuse(vec3 albedo, vec3 specularCoefficient) {
	return albedo * vec3(1.0 - specularCoefficient);
}

/**
 * For direct lighting, performs all computing
 * lightDir is not the light direction but the point-light vector
*/
vec3 computeLightContribution(vec3 normal, vec3 lightDir, vec3 viewDir, vec3 albedo, vec3 lightColor, float metallic, float roughness) {
	// Word "half" reserved, so halfDir
	vec3 halfDir = normalize(lightDir + viewDir);

	float NdotL = clamp(dot(normal, lightDir), 0.0, 1.0);
	float NdotV = clamp(dot(normal, viewDir), 0.0, 1.0);
	float NdotH = clamp(dot(normal, halfDir), 0.0, 1.0);
	float VdotH = clamp(dot(viewDir, halfDir), 0.0, 1.0);
	float HdotL = clamp(dot(halfDir, lightDir), 0.0, 1.0);
	float LdotV = clamp(dot(lightDir, viewDir), 0.0, 1.0);
	float alpha = max(0.0001, roughness * roughness); // Testsquare

	// Compute diffuse and specular
	vec3 specularCoefficient = cookTorrance(albedo, NdotH, NdotL, NdotV, VdotH, alpha, metallic);
	//specularCoefficient = vec3(0.0);

	vec3 diffuseCoefficient = conservativeDiffuse(albedo, specularCoefficient);

	return lightColor * albedo * NdotL;
	//return lightColor * NdotL * (specularCoefficient + diffuseCoefficient);
}