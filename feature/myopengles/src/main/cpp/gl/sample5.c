#include "sample.h"

typedef struct {
    GLint attr_pos;
    GLint attr_uv;
    GLuint unif_matrix;
    GLfloat rotate;
    GLuint texture_id;
} Extension_Sample5;

void sample5_initialize(GLApp *app) {
    __android_log_print(ANDROID_LOG_INFO, "glapp", "initialize");

    app->ext = (Extension_Sample5*) malloc(sizeof(Extension_Sample5));
    Extension_Sample5 *ext = app->ext;

    // 初期化
    ext->rotate = 0;

    const GLchar *vertex_shader_source =
            "uniform mediump mat4 unif_matrix;"
            "attribute mediump vec4 attr_pos;"
            "attribute mediump vec2 attr_uv;"
            "varying mediump vec2 vary_uv;"
            "void main() {"
            "  gl_Position = unif_matrix * attr_pos;"
            "  vary_uv = attr_uv;"
            "}";

    const GLchar *fragment_shader_source =
            "uniform sampler2D texture;"
            "varying mediump vec2 vary_uv;"
            "void main() {"
            "  gl_FragColor = texture2D(texture, vary_uv);"
            "}";

    app->shader_program = Shader_createProgramFromSource(vertex_shader_source, fragment_shader_source);

    ext->attr_pos = glGetAttribLocation(app->shader_program, "attr_pos");
    assert(ext->attr_pos >= 0);
    ext->attr_uv = glGetAttribLocation(app->shader_program, "attr_uv");
    assert(ext->attr_uv >= 0);

    ext->unif_matrix = glGetUniformLocation(app->shader_program, "unif_matrix");
    assert(ext->unif_matrix >= 0);

    // テクスチャ
    {
        glGenTextures(1, &ext->texture_id);
        assert(ext->texture_id >= 0);
        assert(glGetError() == GL_NO_ERROR);

        glBindTexture(GL_TEXTURE_2D, ext->texture_id);
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

    glUseProgram(app->shader_program);
    assert(glGetError() == GL_NO_ERROR);
}
void sample5_resized(GLApp* app, int width, int height) {
    __android_log_print(ANDROID_LOG_INFO, "glapp", "resized() width:%d, height:%d", width, height);

    glViewport(0, 0, width, height);
}

void sample5_rendering(GLApp* app, int width, int height) {
    glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);

    Extension_Sample5 *ext = (Extension_Sample5*)app->ext;

    glEnableVertexAttribArray(ext->attr_pos);
    glEnableVertexAttribArray(ext->attr_uv);

    {
        const GLfloat position[] = {
                // 左上
                -0.5f, 0.5f,
                // 左下
                -0.5f, -0.5f,
                // 右上
                0.5f, 0.5f,
                // 右下
                0.5f, -0.5f
        };

        const GLfloat uv[] = {
                // 左上
                0, 0,
                // 左下
                0, 1,
                // 右上
                1, 0,
                // 右下
                1, 1
        };

        glVertexAttribPointer(ext->attr_pos, 2, GL_FLOAT, GL_FALSE, 0, (GLvoid*) position);
        glVertexAttribPointer(ext->attr_uv, 2, GL_FLOAT, GL_FALSE, 0, (GLvoid*) uv);

        // 行列をアップロードする
        {
            // 移動行列
            const mat4 translate = mat4_translate(0.5f, 0.5f, 0.0f);
            // 拡大縮小行列
            const mat4 scale = mat4_scale(0.5f, 2.0f, 1.0f);
            // 回転行列
            const mat4 rotate = mat4_rotate(vec3_create(0.0f, 0.0f, 1.0f), ext->rotate);

            // シェーダーに渡す前に行列を計算しておく
            mat4 matrix = mat4_multiply(translate, rotate);
            matrix = mat4_multiply(matrix, scale);

            glUniformMatrix4fv(
                    ext->unif_matrix,   // アップロード先
                    1,                  // 行列の個数
                    GL_FALSE,           // 行列を転置する場合はGL_TRUE, しない場合はGL_FALSE
                    (GLfloat*) matrix.m // 行列を格納したGLfloat変数へのポインタ
            );

            // 回転角を増やす
            ext->rotate += 1.0f;
        }

        glBindTexture(GL_TEXTURE_2D, ext->texture_id);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    }

    postFrontBuffer(app);
}

void sample5_destroy(GLApp* app) {
    __android_log_print(ANDROID_LOG_INFO, "glapp", "destroy");
    Extension_Sample5 *ext = (Extension_Sample5*)app->ext;

    glUseProgram(0);
    assert(glGetError() == GL_NO_ERROR);

    glDeleteProgram(app->shader_program);
    assert(glGetError() == GL_NO_ERROR);

    glDeleteTextures(1, ext->texture_id);

    free(app->shader_program);
}
