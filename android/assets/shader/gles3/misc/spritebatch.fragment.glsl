uniform sampler2D u_texture;

in vec4 v_color;
in vec2 v_texCoords;

out vec4 fragmentColor;

void main () {
	fragmentColor = v_color * texture(u_texture, v_texCoords);
}