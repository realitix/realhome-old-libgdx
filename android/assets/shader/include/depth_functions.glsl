// https://mynameismjp.wordpress.com/2009/05/05/reconstructing-position-from-depth-continued/
//http://stackoverflow.com/questions/28066906/reconstructing-world-position-from-linear-depth

vec3 getViewRay(vec3 frustumCorner, vec3 cameraPosition) {
	return frustumCorner - cameraPosition;
}

vec3 getPositionFromDepth(float depth, vec3 ray, vec3 cameraPosition, ) {
	float realDepth = cameraNear + depth * (cameraFar - cameraNear);
	return normalize(ray) * realDepth + cameraPosition;
}