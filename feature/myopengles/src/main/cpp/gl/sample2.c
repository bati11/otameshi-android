#include "sample.h"

typedef struct {
    GLint attr_pos;
    GLubyte attr_color;
    GLint unif_move_pos;
    vec2 pos;
} Extension_Sample2;

GLApp* sample2_initialize(GLApp* app) {
    __android_log_print(ANDROID_LOG_INFO, "glapp", "initialize");

    app->ext = (Extension_Sample2*) malloc(sizeof(Extension_Sample2));
    Extension_Sample2 *ext = app->ext;

    // 頂点シェーダー
    // このサンプルではrendering時に、2次元の頂点を3つ与えられるので、3回呼ばれる（GPUによって並列で実行される）
    const GLchar *vertex_shader_source = "attribute mediump vec4 attr_pos;"    // 中精度のvec4型、変数名はattr_pos
                                         "attribute lowp vec4 attr_color;"     // 頂点カラー
                                         "varying lowp vec4 vary_color;"       // "varying"で頂点シェーダーからフラグメントシェーダーへ値を渡す（この時値が変化する）
                                         "uniform mediump vec2 unif_move_pos;" // 頂点位置を動かす
                                         "void main() {"
                                         "  gl_Position = attr_pos;"
                                         "  gl_Position.xy += unif_move_pos;"
                                         "  vary_color = attr_color;" // 頂点カラー
                                         "}";

    // フラグメントシェーダー（ピクセルシェーダーとも呼ばれる）
    // 塗りつぶされるピクセルの数だけ呼び出される
    // FHDサイズのディスプレイだと 1920 * 1080 = 124,416,000回呼ばれる（GPUによって並列で実行される）
    const GLchar *fragment_shader_source = "varying lowp vec4 vary_color;"
                                           "void main() {"
                                           "    gl_FragColor = vary_color;"
                                           "}";

    // コンパイルとリンクを行う
    app->shader_program = Shader_createProgramFromSource(
            vertex_shader_source,
            fragment_shader_source);

    // attribute変数(頂点ごとの情報)
    ext->attr_pos = glGetAttribLocation(app->shader_program, "attr_pos");
    assert(ext->attr_pos >= 0);
    ext->attr_color = glGetAttribLocation(app->shader_program, "attr_color");
    assert(ext->attr_color >= 0);

    // uniform変数(アプリレベルの情報)
    ext->unif_move_pos = glGetUniformLocation(app->shader_program, "unif_move_pos");
    assert(ext->unif_move_pos >= 0);

    // シェーダー利用開始
    glUseProgram(app->shader_program);
    assert(glGetError() == GL_NO_ERROR);

    ext->pos.x = 0;
    ext->pos.y = 0;

    return app;
}

void sample2_resized(GLApp* app, int width, int height) {
    __android_log_print(ANDROID_LOG_INFO, "glapp", "resized() width:%d, height:%d", width, height);

    // サーフェスの幅と高さと合うようにViewportを変更する
    // Viewportを使って「正規化されたデバイス座標系」→「ウィンドウ座標系」に変換する
    glViewport(0, 0, width, height);
}

void sample2_rendering(GLApp* app, int width, int height) {
    glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);

    Extension_Sample2 *ext = (Extension_Sample2*)app->ext;

    // attribute変数を有効にする
    glEnableVertexAttribArray(ext->attr_pos);
    glEnableVertexAttribArray(ext->attr_color);

    const GLfloat position[] = {
            0.0f, 0.5f,
            0.0f, 0.0f,
            0.5f, 0.5f,
            0.5f, 0.0f,
    };
    const GLubyte color[] = {
            255, 0, 0,
            0, 255, 0,
            0, 0, 255,
            255, 255, 255,
    };
    glVertexAttribPointer(ext->attr_pos, 2, GL_FLOAT, GL_FALSE, 0, (GLvoid*)position);
    glVertexAttribPointer(ext->attr_color, 3, GL_UNSIGNED_BYTE, GL_TRUE, 0, color);

    {
        ext->pos.x += 0.01f;
        ext->pos.y += 0.02f;

        if (ext->pos.x > 1.0f) {
            ext->pos.x = -1;
        }
        if (ext->pos.y > 1.0f) {
            ext->pos.y = -1;
        }

        glUniform2f(ext->unif_move_pos, ext->pos.x, ext->pos.y);
    }

    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

    // バックバッファをフロントバッファへ転送（プラットフォーム依存）
    postFrontBuffer(app);
}

void sample2_destroy(GLApp* app) {
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
