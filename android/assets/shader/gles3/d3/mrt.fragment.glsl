in vec4 v_color;

layout(location = 0) out vec4 gbuffer0;

void main() {
	gbuffer0 = v_color;
}
