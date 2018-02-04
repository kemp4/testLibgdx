//#version 120
//
//in vec3 normal;
//in vec3 fragPos;
//in vec4 ourColor;
//
//out vec4 color;
//
//uniform vec3 lightColor;
//
//void main()
//{
//	//diffuse
//	vec3 lightPos= vec3(0,0,3.0);
//	vec3 norm = normalize(normal);
//	vec3 lightDir = normalize(lightPos - fragPos);
//	float diff = max(dot(norm, lightDir), 0.0);
//	vec3 diffuse = diff * lightColor;
//	color = vec4(0.6,0.5,0.5,1.0);//vec4((lightColor + diffuse),1)*ourColor;
//
//}

#version 330
out vec4 FragColor;
in vec3 normal;
in vec3 fragPos;
in vec4 ourColor;
void main()
{
    vec3 ambient = vec3(0.4,0.4,0.4);
	//diffuse

	vec3 lightColor = vec3(0.6,0.6,0.6);
	vec3 lightPos= vec3(-5,-5,-10);
	vec3 norm = normalize(normal);// not neccesary
	vec3 lightDir = normalize(lightPos - fragPos);
	float diff = max(dot(norm, lightDir), 0.0);
	vec3 diffuse = diff * lightColor;
    FragColor = vec4((ambient + diffuse),1)*ourColor;
}