#version 400

uniform sampler2D tex;
uniform vec3 colour;

in vec2 texCoordForFP;

out vec4 fragColor;

void main(void) {
   fragColor = vec4(colour,1) * texture( tex, texCoordForFP );
}