#version 400

uniform vec3 kd;
uniform vec3 lightCol;
uniform vec3 color;

in vec3 normalForFP;
in vec3 pos;

out vec4 fragColor;

// TODO: Objective 7, GLSL lighting

void main(void) {
   //fragColor = vec4( normalForFP, 1 );
	fragColor = vec4( kd * normalForFP.zzz, 1 );

   
}