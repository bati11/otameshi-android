#include <jni.h>

#include "support.h"
#include "sample.h"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_info_bati11_opengles_myopengles_glapp_NDKApplication_initializeNative(
        JNIEnv* env,
        jclass clazz
) {
    ndk_support_initialize(env);
}

JNIEXPORT void JNICALL Java_info_bati11_opengles_myopengles_glapp_NDKApplication_initialize(
        JNIEnv* env,
        jobject _this
) {
    sample1_initialize();
}

JNIEXPORT void JNICALL Java_info_bati11_opengles_myopengles_glapp_NDKApplication_resized(
        JNIEnv* env,
        jobject _this,
        jint width,
        jint height
) {
    sample1_resized(width, height);
}

JNIEXPORT void JNICALL Java_info_bati11_opengles_myopengles_glapp_NDKApplication_rendering(
        JNIEnv* env,
        jobject _this,
        jint width,
        jint height
) {
    sample1_rendering(width, height);
}

JNIEXPORT void JNICALL Java_info_bati11_opengles_myopengles_glapp_NDKApplication_destroy(
        JNIEnv* env,
        jobject _this
) {
    sample1_destroy();
}

#ifdef __cplusplus
}
#endif
