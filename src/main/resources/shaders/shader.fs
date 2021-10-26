#version 120

uniform sampler2D sampler;//Sampler = location pr texture
varying vec2 tex_coords;

void main() {
    gl_FragColor = texture2D(sampler,tex_coords);
}