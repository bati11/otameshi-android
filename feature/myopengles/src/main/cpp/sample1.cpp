#include "sample.h"

GLApp* sample1_initialize() {
    __android_log_print(ANDROID_LOG_INFO, "glapp", "initialize");

    auto app = (GLApp*)malloc(sizeof(GLApp));

    // 頂点シェーダー
    const GLchar *vertex_shader_source = "attribute mediump vec4 attr_pos;" // 中精度のvec4型、変数名はattr_pos
                                         "void main() {"
                                         "  gl_Position = attr_pos;"
                                         "}";
    app->vert_shader = glCreateShader(GL_VERTEX_SHADER);
    assert(glGetError() == GL_NO_ERROR);
    assert(app->vert_shader != 0);
    glShaderSource(app->vert_shader, 1, &vertex_shader_source, NULL);
    glCompileShader(app->vert_shader);

    {
        // コンパイルエラーをチェックする
        GLint compileSuccess = 0;
        glGetShaderiv(app->vert_shader, GL_COMPILE_STATUS, &compileSuccess);
        if (compileSuccess == GL_FALSE) {
            GLint infoLen = 0;
            glGetShaderiv(app->vert_shader, GL_INFO_LOG_LENGTH, &infoLen);
            if (infoLen > 1) {
                GLchar *message = (GLchar*) calloc(infoLen, sizeof(GLchar));
                glGetShaderInfoLog(app->vert_shader, infoLen, NULL, message);
                __android_log_print(ANDROID_LOG_INFO, "glapp", "%s", message);
                free((void*) message);
            } else {
                __android_log_print(ANDROID_LOG_INFO, "glapp", "compile error(%s)", "vert_shader");
            }
        }
        assert(compileSuccess == GL_TRUE);
    }

    // フラグメントシェーダー（ピクセルシェーダーとも呼ばれる）
    // 塗りつぶされるピクセルの数だけ呼び出される
    // FHDサイズのディスプレイだと 1920 * 1080 = 124,416,000回呼ばれる
    const GLchar *fragment_shader_source = "void main() {"
                                           "    gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);"
                                           "}";
    app->frag_shader = glCreateShader(GL_FRAGMENT_SHADER);
    assert(app->frag_shader != 0);
    glShaderSource(app->frag_shader, 1, &fragment_shader_source, NULL);
    glCompileShader(app->frag_shader);

    {
        // コンパイルエラーをチェックする
        GLint compileSuccess = 0;
        glGetShaderiv(app->frag_shader, GL_COMPILE_STATUS, &compileSuccess);
        if (compileSuccess == GL_FALSE) {
            GLint infoLen = 0;
            glGetShaderiv(app->frag_shader, GL_INFO_LOG_LENGTH, &infoLen);
            if (infoLen > 1) {
                GLchar *message = (GLchar*) calloc(infoLen, sizeof(GLchar));
                glGetShaderInfoLog(app->frag_shader, infoLen, NULL, message);
                __android_log_print(ANDROID_LOG_INFO, "glapp", "%s", message);
                free((void*) message);
            } else {
                __android_log_print(ANDROID_LOG_INFO, "glapp", "compile error(%s)", "frag_shader");
            }
        }
        assert(compileSuccess == GL_TRUE);
    }

    // リンクを行う
    app->shader_program = glCreateProgram();
    assert(app->shader_program != 0);
    glAttachShader(app->shader_program, app->vert_shader);
    assert(glGetError() == GL_NO_ERROR);
    glAttachShader(app->shader_program, app->frag_shader);
    assert(glGetError() == GL_NO_ERROR);

    glLinkProgram(app->shader_program);

    // リンクエラーをチェックする
    {
        GLint linkSuccess = 0;
        glGetProgramiv(app->shader_program, GL_LINK_STATUS, &linkSuccess);
        if (linkSuccess == GL_FALSE) {
            GLint infoLen = 0;
            glGetProgramiv(app->shader_program, GL_INFO_LOG_LENGTH, &infoLen);
            if (infoLen > 1) {
                GLchar *message = (GLchar*) calloc(infoLen, sizeof(GLchar));
                glGetProgramInfoLog(app->shader_program, infoLen, NULL, message);
            }
        }
        assert(linkSuccess == GL_TRUE);
    }

    app->attr_pos = glGetAttribLocation(app->shader_program, "attr_pos");
    assert(app->attr_pos >= 0);

    glUseProgram(app->shader_program);
    assert(glGetError() == GL_NO_ERROR);

    return app;
}

void sample1_resized(GLApp* app, int width, int height) {
    __android_log_print(ANDROID_LOG_INFO, "glapp", "resized() width:%d, height:%d", width, height);

    // サーフェスの幅と高さと合うようにViewportを変更する
    // Viewportを使って「正規化されたデバイス座標系」→「ウィンドウ座標系」に変換する
    glViewport(0, 0, width, height);
}

void sample1_rendering(GLApp* app, int width, int height) {
    glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);

    glEnableVertexAttribArray(app->attr_pos);
    // 正規化されたデバイス座標系
    const GLfloat position[] = {
            0.0f, 1.0f,
            1.0f, -1.0f,
            -1.0f, -1.0f
    };
    glVertexAttribPointer(
            app->attr_pos, // 頂点データを関連づけるattribute変数
            2,             // 頂点データの要素数（2次元なので x,y の2つ）
            GL_FLOAT,      // 頂点データの型
            GL_FALSE,      // normalized 頂点データを正規化して頂点シェーダーに渡す場合はGL_TRUE
            0,             // 頂点の先頭位置ごとのオフセット値
            (GLvoid*) position);
    glDrawArrays(GL_TRIANGLES, 0, 3);

    postFrontBuffer();
}

void sample1_destroy(GLApp* app) {
    __android_log_print(ANDROID_LOG_INFO, "glapp", "dhhestroy");

    // シェーダーの利用を終了する
    glUseProgram(0);
    assert(glGetError() == GL_NO_ERROR);

    // シェーダープログラムを破棄する
    glDeleteProgram(app->shader_program);
    assert(glGetError() == GL_NO_ERROR);
    assert(glIsProgram(app->shader_program) == GL_FALSE);

    // シェーダーオブジェクトを破棄する
    glDeleteShader(app->vert_shader);
    assert(glGetError() == GL_NO_ERROR);
    glDeleteShader(app->frag_shader);
    assert(glGetError() == GL_NO_ERROR);

    free(app);
}
