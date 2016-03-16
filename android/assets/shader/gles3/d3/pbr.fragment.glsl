uniform sampler2D u_gbuffer0;

in vec2 v_uv;

out vec4 fragmentColor;

void main() {
	fragmentColor = texture(u_gbuffer0, v_uv);
}
