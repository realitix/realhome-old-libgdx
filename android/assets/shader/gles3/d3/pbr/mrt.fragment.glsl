in vec4 v_color;
in vec3 v_normal;

layout(location = 0) out vec4 gbuffer0;
layout(location = 1) out vec4 gbuffer1;

void main() {
	gbuffer0 = v_color;
	gbuffer1 = vec4(v_normal, 1.0);
}
