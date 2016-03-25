// https://mynameismjp.wordpress.com/2009/05/05/reconstructing-position-from-depth-continued/
//http://stackoverflow.com/questions/28066906/reconstructing-world-position-from-linear-depth

vec3 getViewRay(vec2 frustumCorner, mat4 viewTrans) {
	// Corner position in view space
	return inverse(viewTrans) * vec4(frustumCorner, 0.0, 1.0);
}

vec3 getPositionFromDepth(float depth, vec3 ray, vec3 cameraPosition) {
	return cameraPosition + depth * normalize(ray);
}