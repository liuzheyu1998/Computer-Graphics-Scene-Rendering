#version 400

uniform vec3 kd;
uniform vec3 lightCol;
uniform vec3 lightDir;
uniform vec3 viewDir;
uniform vec3 amb;

in vec3 normalForFP;
in vec3 pos;

out vec4 fragColor;

// TODO: Objective 7, GLSL lighting

void main(void) {
   //fragColor = vec4( normalForFP, 1 );
    vec3 halfAngle = normalize(lightDir+viewDir);
	float diffuse = max(0.0, dot(normalForFP, lightDir ));
	vec3 scatteredLight = lightCol * diffuse;
	//float specular = max (0.0, dot(normalForFP, halfvec ));
	float specular = max (0.0, dot(normalForFP, halfAngle ));
	specular = pow(specular, 16);
	
	vec3 reflectedLight = lightCol * specular;

	vec3 result = amb + 0.7 * scatteredLight + 0.3 * reflectedLight;
	fragColor = vec4(result , 1 );

   
}