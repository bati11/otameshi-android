#ifndef OTAMESHIAPP_SAMPLE_HELPER_H
#define OTAMESHIAPP_SAMPLE_HELPER_H

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
