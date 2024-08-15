#ifndef OTAMESHIAPP_SAMPLE_HELPER_H
#define OTAMESHIAPP_SAMPLE_HELPER_H

#include <math.h>
#include <stdlib.h>
#include <assert.h>
#include <string.h>

#include <android/log.h>

#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

#include "GLApp.h"

/**
 * シェーターのコンパイルとリンク
 */
GLuint Shader_createProgramFromSource(const GLchar* vertex_shader_source, const GLchar* fragment_shader_source);

/**
 * 2次元ベクトルを保持する構造体
 */
typedef struct vec2 {
    GLfloat x;
    GLfloat y;
} vec2;

/**
 * 3次元ベクトルを保持する構造体
 */
typedef struct vec3 {
    GLfloat x;
    GLfloat y;
    GLfloat z;
} vec3;

vec3 vec3_create(const GLfloat x, const GLfloat y, const GLfloat z);
GLfloat vec3_length(const vec3 v);
vec3 vec3_normalize(const vec3 v);
vec3 vec3_createNormalized(const GLfloat x, const GLfloat y, const GLfloat z);

/**
 * 行列を表す構造体
 */
typedef struct mat4 {
    GLfloat m[4][4];
} mat4;

/**
 * 単位行列をつくる
 */
mat4 mat4_identity();

/**
 * 移動行列をつくる
 */
mat4 mat4_translate(const GLfloat x, const GLfloat y, const GLfloat z);

/**
 * 拡大縮小行列
 */
mat4 mat4_scale(const GLfloat x, const GLfloat y, const GLfloat z);

/**
 * 回転行列
 */
mat4 mat4_rotate(const vec3 axis, const GLfloat rotate);

/**
 * 行列×行列
 */
mat4 mat4_multiply(const mat4 a, const mat4 b);

/**
 * RGBA各8bit色
 *
 * 32bit / pixel
 */
#define TEXTURE_RAW_RGBA8       0

/**
 * RGB各8bit色
 *
 * 24bit / pixel
 */
#define TEXTURE_RAW_RGB8        1

/**
 * RGB各5bit
 * A 1bit
 *
 * 16bit / pixel
 */
#define TEXTURE_RAW_RGBA5551    2

/**
 * R 5bit
 * G 6bit
 * B 5bit
 *
 * 16bit/ pixel
 */
#define TEXTURE_RAW_RGB565      3

/**
 * 非圧縮のRawピクセル情報（=RGBAの色情報配列）
 */
typedef struct RawPixelImage {
    /**
     * ピクセル配列
     */
    void* pixel_data;

    /**
     * 画像幅
     */
    int width;

    /**
     * 画像高さ
     */
    int height;

    /**
     * 画像フォーマット
     */
    int format;
} RawPixelImage;

#endif //OTAMESHIAPP_SAMPLE_HELPER_H
