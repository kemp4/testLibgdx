//#version 120
//attribute vec3 a_position;
//attribute vec4 a_color;
//attribute vec3 a_normal;
//
//out vec3 normal;
//out vec3 fragPos;
//out vec4 ourColor;
//
//uniform mat4 projection;
//
//void main()
//{
//	gl_Position = projection * vec4(a_position, 1.0f);
//    ourColor = a_color;
// 	normal =  a_normal;
// 	fragPos = a_position;
// }
//
#version 330
layout(location = 0) in vec3 a_position;
layout(location = 1) in vec4 a_color;
layout(location = 2) in vec3 a_normal;
uniform mat4 combined;
out vec3 normal;
out vec3 fragPos;
out vec4 ourColor;
 void main()
 {
    normal = a_normal;
    ourColor = a_color;
    fragPos = a_position;
    gl_Position = combined * vec4(a_position, 1.0);
 }