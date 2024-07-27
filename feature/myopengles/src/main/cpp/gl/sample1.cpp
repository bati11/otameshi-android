#include "sample.h"

GLApp* sample1_initialize() {
    __android_log_print(ANDROID_LOG_INFO, "glapp", "initialize");

    auto app = (GLApp*)malloc(sizeof(GLApp));

    // 頂点シェーダー
    const GLchar *vertex_shader_source = "attribute mediump vec4 attr_pos;" // 中精度のvec4型、変数名はattr_pos
                                         "void main() {"
                                         "  gl_Position = attr_pos;"
                                         "}";

    // フラグメントシェーダー（ピクセルシェーダーとも呼ばれる）
    // 塗りつぶされるピクセルの数だけ呼び出される
    // FHDサイズのディスプレイだと 1920 * 1080 = 124,416,000回呼ばれる
    const GLchar *fragment_shader_source = "void main() {"
                                           "    gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);"
                                           "}";

    // リンクを行う
    app->shader_program = Shader_createProgramFromSource(
            vertex_shader_source,
            fragment_shader_source);

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

    postFrontBuffer(app);
}

void sample1_destroy(GLApp* app) {
    __android_log_print(ANDROID_LOG_INFO, "glapp", "destroy");

    // シェーダーの利用を終了する
    glUseProgram(0);
    assert(glGetError() == GL_NO_ERROR);

    // シェーダープログラムを破棄する
    glDeleteProgram(app->shader_program);
    assert(glGetError() == GL_NO_ERROR);
    assert(glIsProgram(app->shader_program) == GL_FALSE);

    free(app);
}
