#include <cassert>
#include <string>

#include <android/log.h>
#include "GLApp.h"

GLuint Shader_createProgramFromSource(const GLchar* vertex_shader_source, const GLchar* fragment_shader_source);

void postFrontBuffer(GLApp* app);

GLApp* sample1_initialize();
void sample1_resized(GLApp* app, int width, int height);
void sample1_rendering(GLApp* app, int width, int height);
void sample1_destroy(GLApp* app);
