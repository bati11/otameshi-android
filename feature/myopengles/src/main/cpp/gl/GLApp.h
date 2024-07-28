#ifndef OTAMESHIAPP_GLAPP_H
#define OTAMESHIAPP_GLAPP_H

#include "jni.h"
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

typedef struct {
    jobject japp;
    GLuint shader_program;
    void* ext;
} GLApp;

#endif //OTAMESHIAPP_GLAPP_H
