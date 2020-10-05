#version 400

uniform vec3 kd;
uniform vec3 ks;
uniform vec3 lightCol1;
uniform vec3 lightDir1;
uniform vec3 lightCol2;
uniform vec3 lightDir2;
uniform vec3 lightCol3;
uniform vec3 lightDir3;


uniform vec3 viewDir;
uniform vec3 ambient;

uniform vec3 color;

in vec3 normalForFP;
in vec3 pos;

out vec4 fragColor;

// TODO: Objective 7, GLSL lighting

void main(void) {
   //fragColor = vec4( normalForFP, 1 );
 	vec3 halfAngle1 = normalize(lightDir1 + viewDir);
 	vec3 normalLightDir1 = normalize(lightDir1);
 	float diffuse1 = max(0.0, dot(normalForFP, normalLightDir1 ));
 	float specular1 = max (0.0, dot(normalForFP, halfAngle1 ));
 	specular1 = pow(specular1, 8);
 	vec3 scatteredLight1 = ambient + kd * lightCol1 * diffuse1 ;
 	vec3 reflectedLight1 = ks * lightCol1 * specular1;
 	
 	vec3 halfAngle2 = normalize(lightDir2 + viewDir);
 	vec3 normalLightDir2 = normalize(lightDir2);
 	float diffuse2 = max(0.0, dot(normalForFP, normalLightDir2));
 	float specular2 = max (0.0, dot(normalForFP, halfAngle2 ));
 	specular2 = pow(specular2, 8);
 	vec3 scatteredLight2 = ambient + kd * lightCol2 * diffuse2 ;
 	vec3 reflectedLight2 = ks * lightCol2 * specular2;
 	
 	vec3 halfAngle3 = normalize(lightDir3 + viewDir);
 	vec3 normalLightDir3 = normalize(lightDir3);
 	float diffuse3 = max(0.0, dot(normalForFP, normalLightDir3));
 	float specular3 = max (0.0, dot(normalForFP, halfAngle3 ));
 	specular3 = pow(specular3, 8);
 	vec3 scatteredLight3 = ambient + kd * lightCol3 * diffuse3 ;
 	vec3 reflectedLight3 = ks * lightCol3 * specular3;
 	vec3 result = min(scatteredLight1 + reflectedLight1 + scatteredLight2 + reflectedLight2 + scatteredLight3 + reflectedLight3 ,vec3(1.0));
 	fragColor = vec4(result , 1 );
 	
 	
   
}