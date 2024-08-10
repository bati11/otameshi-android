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
