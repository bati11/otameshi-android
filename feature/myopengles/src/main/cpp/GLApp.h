#ifndef OTAMESHIAPP_GLAPP_H
#define OTAMESHIAPP_GLAPP_H

#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

struct GLApp {
    GLuint shader_program;
    GLuint vert_shader;
    GLuint frag_shader;
    GLint attr_pos;
};

#endif //OTAMESHIAPP_GLAPP_H
