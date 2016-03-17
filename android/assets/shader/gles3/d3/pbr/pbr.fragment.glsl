uniform sampler2D u_gbuffer0;
uniform sampler2D u_gbuffer1;

in vec2 v_uv;

out vec4 fragmentColor;

void main() {
	fragmentColor = texture(u_gbuffer1, v_uv);
}
