#include "sample.h"

typedef struct {
    GLint attr_pos;
    GLint attr_uv;
    GLint unif_tex_color;
    GLint unif_tex_mask;
    GLuint* texture_id[2];
} Extension_Sample4;

void sample4_initialize(GLApp *app) {
    __android_log_print(ANDROID_LOG_INFO, "glapp", "initialize");
    app->ext = (Extension_Sample4*) malloc(sizeof(Extension_Sample4));
    Extension_Sample4 *ext = app->ext;

    const GLchar *vertex_shader_source =
            "attribute mediump vec4 attr_pos;"
            "attribute mediump vec2 attr_uv;"
            "varying mediump vec2 vary_uv;"
            "void main() {"
            "  gl_Position = attr_pos;"
            "  vary_uv = attr_uv;"
            "}";

    const GLchar *fragment_shader_source =
            "uniform sampler2D tex_color;"
            "uniform sampler2D tex_mask;"
            "varying mediump vec2 vary_uv;"
            "void main() {"
            "  if(texture2D(tex_mask, vary_uv).r == 0.0) {"
            "    discard;"
            "  }"
            "  gl_FragColor = texture2D(tex_color, vary_uv);"
            "}";

    app->shader_program = Shader_createProgramFromSource(vertex_shader_source, fragment_shader_source);

    ext->attr_pos = glGetAttribLocation(app->shader_program, "attr_pos");
    assert(ext->attr_pos >= 0);
    ext-> attr_uv = glGetAttribLocation(app->shader_program, "attr_uv");
    assert(ext->attr_uv >= 0);

    ext->unif_tex_color = glGetUniformLocation(app->shader_program, "tex_color");
    ext->unif_tex_mask = glGetUniformLocation(app->shader_program, "tex_mask");
    assert(ext->unif_tex_color >= 0);
    assert(ext->unif_tex_mask >= 0);

    assert(glGetError() == GL_NO_ERROR);

    {
        // 画像Aを読み込む
        glGenTextures(1, &ext->texture_id[0]);
        assert(ext->texture_id[0] >= 0);
        assert(glGetError() == GL_NO_ERROR);

        glBindTexture(GL_TEXTURE_2D, ext->texture_id[0]);
        assert(glGetError() == GL_NO_ERROR);
        RawPixelImage *image = NULL;
        image = loadImage(app, "texture_rgba_512x512.png");
        assert(image != NULL);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image->width, image->height, 0, GL_RGBA,
                     GL_UNSIGNED_BYTE, image->pixel_data);
        assert(glGetError() == GL_NO_ERROR);
        freeImage(app, image);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST); // 拡大描画時の処理方法
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST); // 縮小描画時の処理方法
        assert(glGetError() == GL_NO_ERROR);

        glBindTexture(GL_TEXTURE_2D, 0);
    }
    {
        // マスク画像を読み込む
        glGenTextures(1, &ext->texture_id[1]);
        assert(ext->texture_id[1] >= 0);
        assert(glGetError() == GL_NO_ERROR);

        glBindTexture(GL_TEXTURE_2D, ext->texture_id[1]);
        assert(glGetError() == GL_NO_ERROR);
        RawPixelImage *image = NULL;
        image = loadImage(app, "mask001.png");
        assert(image != NULL);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image->width, image->height, 0, GL_RGBA,
                     GL_UNSIGNED_BYTE, image->pixel_data);
        assert(glGetError() == GL_NO_ERROR);
        freeImage(app, image);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST); // 拡大描画時の処理方法
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST); // 縮小描画時の処理方法
        assert(glGetError() == GL_NO_ERROR);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, ext->texture_id[0]);

    glActiveTexture(GL_TEXTURE1);
    glBindTexture(GL_TEXTURE_2D, ext->texture_id[1]);

    glUseProgram(app->shader_program);
    assert(glGetError() == GL_NO_ERROR);
}

void sample4_resized(GLApp* app, int width, int height) {
    __android_log_print(ANDROID_LOG_INFO, "glapp", "resized() width:%d, height:%d", width, height);

    glViewport(0, 0, width, height);
}

void sample4_rendering(GLApp* app, int width, int height) {
    glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);

    Extension_Sample4 *ext = (Extension_Sample4*)app->ext;

    glEnableVertexAttribArray(ext->attr_pos);
    glEnableVertexAttribArray(ext->attr_uv);

    {
        const GLfloat position[] = {
                // 左上
                -0.95f, 0.95f,
                // 左下
                -0.95f, -0.95f,
                // 右上
                0.95f, 0.95f,
                // 右下
                0.95f, -0.95f
        };
        // 0.0f ~ 1.0f の範囲は設定
        const GLfloat uv[] = {
                // 左上
                0.0f, 0.0f,
                // 左下
                0.0f, 1.0f,
                // 右上
                1.0f, 0.0f,
                // 右下
                1.0f, 1.0f
        };

        glVertexAttribPointer(ext->attr_uv, 2, GL_FLOAT, GL_FALSE, 0, (GLvoid*) uv);
        glVertexAttribPointer(ext->attr_pos, 2, GL_FLOAT, GL_FALSE, 0, (GLvoid*) position);

        // テクスチャを関連づける
        glUniform1i(ext->unif_tex_color, 0);
        glUniform1i(ext->unif_tex_mask, 1);

        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    }

    postFrontBuffer(app);
}
void sample4_destroy(GLApp* app) {
    __android_log_print(ANDROID_LOG_INFO, "glapp", "destroy");
    Extension_Sample4 *ext = (Extension_Sample4*)app->ext;

    glUseProgram(0);
    assert(glGetError() == GL_NO_ERROR);

    glDeleteProgram(app->shader_program);
    assert(glGetError() == GL_NO_ERROR);

    glDeleteTextures(1, ext->texture_id[0]);
    glDeleteTextures(1, ext->texture_id[1]);

    free(app->shader_program);
}
