#version 300 es

in vec4 vertexPosition;

uniform struct{
  mat4 modelMatrix;
} gameObject;

uniform struct{
  mat4 viewProjMatrix;
} camera;

void main(void) {
  gl_Position = vertexPosition * gameObject.modelMatrix;
}