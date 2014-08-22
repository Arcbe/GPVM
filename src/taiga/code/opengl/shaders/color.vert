uniform mat4 projection;
uniform mat4 modelview;

void main() {
    gl_Position = modelview * gl_Vertex;
    gl_Position = projection * gl_Position;
    gl_Position /= gl_Position.w;

    gl_FrontColor = gl_Color;
}