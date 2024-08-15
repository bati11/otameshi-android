#include "sample.h"

typedef struct {
    GLint attr_pos;
    GLint attr_uv;
    GLint unif_texture;
    GLuint texture_id;
} Extension_LoadTexture;

void sample3_initialize(GLApp *app) {
    __android_log_print(ANDROID_LOG_INFO, "glapp", "initialize");

    app->ext = (Extension_LoadTexture*) malloc(sizeof(Extension_LoadTexture));
    Extension_LoadTexture *ext = app->ext;

    const GLchar *vertex_shader_source =
            "attribute mediump vec4 attr_pos;"
            "attribute mediump vec2 attr_uv;"
            "varying mediump vec2 vary_uv;"
            "void main() {"
            "  gl_Position = attr_pos;"
            "  vary_uv = attr_uv;"
            "}";

    const GLchar *fragment_shader_source =
            "uniform sampler2D unif_texture;"
            "varying mediump vec2 vary_uv;"
            "void main() {"
            "  gl_FragColor = texture2D(unif_texture, vary_uv);"
            "}";

    // コンパイルとリンクを行う
    app->shader_program = Shader_createProgramFromSource(vertex_shader_source, fragment_shader_source);

    // attribute変数(頂点ごとの情報)
    ext->attr_pos = glGetAttribLocation(app->shader_program, "attr_pos");
    assert(ext->attr_pos >= 0);
    ext->attr_uv = glGetAttribLocation(app->shader_program, "attr_uv");
    assert(ext->attr_uv >= 0);

    // uniform変数(アプリレベルの情報)
    ext->unif_texture = glGetUniformLocation(app->shader_program, "unif_texture");
    assert(ext->unif_texture >= 0);

    {
        // テクスチャの生成を行う
        glGenTextures(
                1, // 生成するテクスチャオブジェクト数
                &ext->texture_id // テクスチャオブジェクトの格納先ポインタ
        );
        assert(ext->texture_id != 0);
        assert(glGetError() == GL_NO_ERROR);

        glPixelStorei(
                GL_UNPACK_ALIGNMENT, // テクスチャへピクセル情報をアップロード（VRAMへアップロード）する際の設定
                1 // 何バイト教会でピクセル情報が並べられているか
        );
        assert(glGetError() == GL_NO_ERROR);

        glBindTexture(
                GL_TEXTURE_2D, // 2次元テクスチャとして関連づける
                ext->texture_id // テクスチャ系コマンドの対象としてバインドするテクスチャID
        );
        assert(glGetError() == GL_NO_ERROR);

        // 画像ピクセルを読み込む
        RawPixelImage *image = NULL;
        image = loadImage(app, "texture_rgba_512x512.png");
        assert(image != NULL);

        // GPUがアクセスするVRAMへピクセル情報をコピーする。ピクセルだったデータはテクセルとしてGPUから使えるようになる
        glTexImage2D(
                GL_TEXTURE_2D, // テクスチャの利用方法。2次元テクスチャとして読み込む
                0, // 転送先のミップマップレベル
                GL_RGBA, // テクスチャフォーマット。RGBAの画素情報を指定
                image->width, // テクスチャの幅
                image->height, // テクスチャの高さ
                0, // テクスチャのborderピクセル数。OpenGL ESでは0のみ
                GL_RGBA, // RAWピクセル情報のフォーマット。OpenGL ESでは第3引数と同じ値を指定する
                GL_UNSIGNED_BYTE, // RAWピクセル情報の型
                image->pixel_data // VRAMへアップロードしたいピクセル情報のポインタ
        );
        assert(glGetError() == GL_NO_ERROR);

        // VRAMへアップロード後はCPUがアクセスする情報は不要なので解放する
        freeImage(app, image);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST); // 拡大描画時の処理方法
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST); // 縮小描画時の処理方法
        assert(glGetError() == GL_NO_ERROR);
    }

    // シェーダー利用開始
    glUseProgram(app->shader_program);
    assert(glGetError() == GL_NO_ERROR);
}

void sample3_resized(GLApp* app, int width, int height) {
    __android_log_print(ANDROID_LOG_INFO, "glapp", "resized() width:%d, height:%d", width, height);

    // サーフェスの幅と高さと合うようにViewportを変更する
    // Viewportを使って「正規化されたデバイス座標系」→「ウィンドウ座標系」に変換する
    glViewport(0, 0, width, height);
}

void sample3_rendering(GLApp* app, int width, int height) {
    glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);

    Extension_LoadTexture *ext = (Extension_LoadTexture*)app->ext;

    // attribute変数を有効にする
    glEnableVertexAttribArray(ext->attr_pos);
    glEnableVertexAttribArray(ext->attr_uv);

    // テクスチャユニットの番号を伝える
    glUniform1i(ext->unif_texture, 0); // 0番のテクスチャユニット

    const GLfloat position[] = {
            // 左上
            -0.75f, 0.75f,
            // 左下
            -0.75, -0.75,
            // 右上
            0.75f, 0.75f,
            // 右下
            0.75f, -0.75f
    };

    const GLfloat uv[] = {
            // 左上
            0.25f, 0.25f,
            // 左下
            0, 0.75f,
            // 右上
            1, 0.25f,
            // 右下
            0.75f, 0.75f,
    };
    glVertexAttribPointer(ext->attr_pos, 2, GL_FLOAT, GL_FALSE, 0, (GLvoid*) position);
    glVertexAttribPointer(ext->attr_uv, 2, GL_FLOAT, GL_FALSE, 0, (GLvoid*) uv);
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

    // バックバッファをフロントバッファへ転送（プラットフォーム依存）
    postFrontBuffer(app);
}

void sample3_destroy(GLApp* app) {
    __android_log_print(ANDROID_LOG_INFO, "glapp", "destroy");

    Extension_LoadTexture *ext = (Extension_LoadTexture*)app->ext;

    // シェーダーの利用を終了する
    glUseProgram(0);
    assert(glGetError() == GL_NO_ERROR);

    // シェーダープログラムを破棄する
    glDeleteProgram(app->shader_program);
    assert(glGetError() == GL_NO_ERROR);
    assert(glIsProgram(app->shader_program) == GL_FALSE);

    // テクスチャのアンバインド
    glBindTexture(GL_TEXTURE_2D, 0);
    // テクスチャを解放
    glDeleteTextures(1, &ext->texture_id);
    assert(glGetError() == GL_NO_ERROR);

    free(app);
}