#include <stdlib.h>
#include <assert.h>
#include <string.h>

#include <android/log.h>

#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

#include "GLApp.h"

/**
 * 2次元ベクトルを保持する構造体
 */
typedef struct vec2 {
    /**
     * X値
     */
    GLfloat x;

    /**
     * Y値
     */
    GLfloat y;
} vec2;

GLuint Shader_createProgramFromSource(const GLchar* vertex_shader_source, const GLchar* fragment_shader_source);
