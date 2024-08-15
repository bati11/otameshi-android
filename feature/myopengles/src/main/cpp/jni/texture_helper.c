#include "./support.h"

void convertColorRGBA(const void *rgba8888_pixels, const int pixel_format, void *dst_pixels, const int pixel_num) {
    int pixels = pixel_num;
    unsigned char *src_rgba8888 = (unsigned char *) rgba8888_pixels;

    switch (pixel_format) {
        case TEXTURE_RAW_RGBA8:
            memcpy(dst_pixels, src_rgba8888, pixels * 4);
            break;
        default:
            // 他のpixel_formatは未実装
            assert(pixel_format == TEXTURE_RAW_RGBA8);
            break;
    }
}

RawPixelImage* loadImage(GLApp* app, const char* file_name) {
    // 1ピクセルあたり RGBA 8bitずつ 合計32ビット とする
    const int pixel_format = TEXTURE_RAW_RGBA8;

    // 1ピクセルあたりのバイトサイズ
    int pixelSize = 0;
    switch (pixel_format) {
        case TEXTURE_RAW_RGBA8:
            pixelSize = 4;
            break;
        case TEXTURE_RAW_RGB8:
            pixelSize = 3;
            break;
        case TEXTURE_RAW_RGB565:
        case TEXTURE_RAW_RGBA5551:
            pixelSize = 2;
            break;
    }
    assert(pixelSize > 0);

    JNIEnv *env = currentJNIEnv();
    jmethodID method_loadImage = JniApplication_methodID(env, "loadImage", "(Ljava/lang/String;I)Linfo/bati11/opengles/myopengles/glapp/jni/RawPixelImage;");
    jobject japp = app->japp; // FIXME ここがまだnull
    jstring jFileName = (*env)->NewStringUTF(env, file_name);

    // Java側の画像保持オブジェクトを取得
    jobject jRawImage = (*env)->CallObjectMethod(env, japp, method_loadImage, jFileName, pixel_format);
    if (!jRawImage) {
        __android_log_print(ANDROID_LOG_INFO, "failed loadImage", NULL);
        return NULL;
    }
    static jfieldID field_width = NULL;
    static jfieldID field_height = NULL;
    static jfieldID field_pixel_data = NULL;
    if (!field_pixel_data) {
        jclass clazz = (*env)->GetObjectClass(env, jRawImage);
        field_width = (*env)->GetFieldID(env, clazz, "width", "I");
        field_height = (*env)->GetFieldID(env, clazz, "height", "I");
        field_pixel_data = (*env)->GetFieldID(env, clazz, "pixelData", "Ljava/nio/Buffer;");
        assert(field_width != NULL);
        assert(field_height != NULL);
        assert(field_pixel_data != NULL);

        (*env)->DeleteLocalRef(env, clazz);
    }

    RawPixelImage *image = (RawPixelImage*) malloc(sizeof(RawPixelImage));
    image->format = pixel_format;
    image->width = (*env)->GetIntField(env, jRawImage, field_width);
    image->height = (*env)->GetIntField(env, jRawImage, field_height);

    // 画像のメモリ
    image->pixel_data = (void*) malloc(image->width * image->height * pixelSize);
    // Java側のBuffer
    jobject jpixel_data = (*env)->GetObjectField(env, jRawImage, field_pixel_data);
    // メモリアドレスを確保
    void* pixelbuffer = (*env)->GetDirectBufferAddress(env, jpixel_data);

    // 圧縮状態のピクセル情報(=AndroidのBitmapオブジェクトで表現)を、非圧縮のRawピクセル情報(=RGBAの色情報配列)に変換する
    convertColorRGBA(pixelbuffer, pixel_format, image->pixel_data, image->width * image->height);

    (*env)->DeleteLocalRef(env, jpixel_data);
    (*env)->DeleteLocalRef(env, jFileName);
    (*env)->DeleteLocalRef(env, jRawImage);

    return image;
}

void freeImage(GLApp* app, RawPixelImage* image) {
    if (image) {
        if (image->pixel_data) {
            free(image->pixel_data);
            image->pixel_data = NULL;
        }
        free(image);
    }
}

