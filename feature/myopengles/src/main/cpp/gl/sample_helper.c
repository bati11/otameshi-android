#include "sample_helper.h"

GLint Shader_compile(const GLchar* shader_source, GLenum GL_XXXX_SHADER) {
    const GLuint shader = glCreateShader(GL_XXXX_SHADER);
    assert(glGetError() == GL_NO_ERROR);

    glShaderSource(shader, 1, &shader_source, NULL);
    glCompileShader(shader);

    {
        // コンパイルエラーをチェックする)
        GLint compileSuccess = 0;
        glGetShaderiv(shader, GL_COMPILE_STATUS, &compileSuccess);
        if (compileSuccess == GL_FALSE) {
            GLint infoLen = 0;
            glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);
            if (infoLen > 1) {
                GLchar *message = (GLchar*) calloc(infoLen, sizeof(GLchar));
                glGetShaderInfoLog(shader, infoLen, NULL, message);
                __android_log_print(ANDROID_LOG_INFO, "glapp", "%s", message);
                free((void*) message);
            } else {
                __android_log_print(ANDROID_LOG_INFO, "glapp", "compile error(%s)", "vert_shader");
            }
        }
        assert(compileSuccess == GL_TRUE);
    }

    return shader;
}

GLuint Shader_createProgramFromSource(const GLchar* vertex_shader_source, const GLchar* fragment_shader_source) {
    const GLuint vertex_shader = Shader_compile(vertex_shader_source, GL_VERTEX_SHADER);
    const GLuint fragment_shader = Shader_compile(fragment_shader_source, GL_FRAGMENT_SHADER);

    const GLuint program = glCreateProgram();
    assert(glGetError() == GL_NO_ERROR);
    assert(program != 0);

    glAttachShader(program, vertex_shader);
    assert(glGetError() == GL_NO_ERROR);

    glAttachShader(program, fragment_shader);
    assert(glGetError() == GL_NO_ERROR);

    glLinkProgram(program);

    // リンクエラーをチェックする
    {
        GLint linkSuccess = 0;
        glGetProgramiv(program, GL_LINK_STATUS, &linkSuccess);
        if (linkSuccess == GL_FALSE) {
            GLint infoLen = 0;
            glGetProgramiv(program, GL_INFO_LOG_LENGTH, &infoLen);
            if (infoLen > 1) {
                GLchar *message = (GLchar*) calloc(infoLen, sizeof(GLchar));
                glGetProgramInfoLog(program, infoLen, NULL, message);
            }
        }
        assert(linkSuccess == GL_TRUE);
    }

    glDeleteShader(vertex_shader);
    glDeleteShader(fragment_shader);

    return program;
}

vec3 vec3_create(const GLfloat x, const GLfloat y, const GLfloat z) {
    vec3 v = { x, y, z };
    return v;
}

GLfloat vec3_length(const vec3 v) {
    return (GLfloat) sqrt(((double) v.x * (double) v.x) +
                          ((double) v.y * (double) v.y) +
                          ((double) v.z * (double) v.z));
}

vec3 vec3_normalize(const vec3 v) {
    const GLfloat len = vec3_length(v);
    return vec3_create(v.x / len, v.y / len, v.z / len);
}

vec3 vec3_createNormalized(const GLfloat x, const GLfloat y, const GLfloat z) {
    return vec3_normalize(vec3_create(x, y, z));
}

mat4 mat4_identity() {
    mat4 result;
    int row = 0;
    int column = 0;
    for (column = 0; column < 4; column++) {
        for (row = 0; row < 4; row++) {
            if (column == row) {
                result.m[column][row] = 1.0f;
            } else {
                result.m[column][row] = 0.0f;
            }
        }
    }
    return result;
}

mat4 mat4_translate(const GLfloat x, const GLfloat y, const GLfloat z) {
    mat4 result = mat4_identity();
    result.m[3][0] = x;
    result.m[3][1] = y;
    result.m[3][2] = z;
    return result;
}

mat4 mat4_scale(const GLfloat x, const GLfloat y, const GLfloat z) {
    mat4 result = mat4_identity();
    result.m[0][0] = x;
    result.m[1][1] = y;
    result.m[2][2] = z;
    return result;
}

#define degree2radian(degree) ((degree * M_PI) / 180.0)

mat4 mat4_rotate(const vec3 axis, const GLfloat rotate) {
    mat4 result;

    const GLfloat x = axis.x;
    const GLfloat y = axis.y;
    const GLfloat z = axis.z;

    const GLfloat co = cos(degree2radian(rotate));
    const GLfloat si = sin(degree2radian(rotate));
    {
        result.m[0][0] = (x * x) * (1.0f - co) + co;
        result.m[0][1] = (x * y) * (1.0f - co) - z * si;
        result.m[0][2] = (x * z) * (1.0f - co) + y * si;
        result.m[0][3] = 0;
    }
    {
        result.m[1][0] = (y * x) * (1.0f - co) + z * si;
        result.m[1][1] = (y * y) * (1.0f - co) + co;
        result.m[1][2] = (y * z) * (1.0f - co) - x * si;
        result.m[1][3] = 0;
    }
    {
        result.m[2][0] = (z * x) * (1.0f - co) - y * si;
        result.m[2][1] = (z * y) * (1.0f - co) + x * si;
        result.m[2][2] = (z * z) * (1.0f - co) + co;
        result.m[2][3] = 0;
    }
    {
        result.m[3][0] = 0;
        result.m[3][1] = 0;
        result.m[3][2] = 0;
        result.m[3][3] = 1;
    }
    return result;
}

mat4 mat4_multiply(const mat4 a, const mat4 b) {
    mat4 result;
    int i = 0;
    for (i = 0; i < 4; i++) {
        result.m[i][0] = a.m[0][0] * b.m[i][0] + a.m[1][0] * b.m[i][1] + a.m[2][0] * b.m[i][2] + a.m[3][0] * b.m[i][3];
        result.m[i][1] = a.m[0][1] * b.m[i][0] + a.m[1][1] * b.m[i][1] + a.m[2][1] * b.m[i][2] + a.m[3][1] * b.m[i][3];
        result.m[i][2] = a.m[0][2] * b.m[i][0] + a.m[1][2] * b.m[i][1] + a.m[2][2] * b.m[i][2] + a.m[3][2] * b.m[i][3];
        result.m[i][3] = a.m[0][3] * b.m[i][0] + a.m[1][3] * b.m[i][1] + a.m[2][3] * b.m[i][2] + a.m[3][3] * b.m[i][3];
    }
    return result;
}
