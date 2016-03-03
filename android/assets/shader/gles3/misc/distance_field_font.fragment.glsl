uniform sampler2D u_texture;
uniform float u_smoothing;

in vec4 v_color;
in vec2 v_texCoords;

out vec4 fragmentColor;

void main() {
	if (u_smoothing > 0.0) {
		float smoothing = 0.25 / u_smoothing;
		float distance = texture(u_texture, v_texCoords).a;
		float alpha = smoothstep(0.5 - smoothing, 0.5 + smoothing, distance);
		fragmentColor = vec4(v_color.rgb, alpha * v_color.a);
	}
	else {
		fragmentColor = v_color * texture(u_texture, v_texCoords);
	}
}