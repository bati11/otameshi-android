#include <jni.h>
#include "GLApp.h"
#include "sample.h"

/** ローカル参照 */
JNIEnv* jenv;
jobject japp;
jmethodID method_postFrontBuffer;
void postFrontBuffer() {
    jenv->CallVoidMethod(japp, method_postFrontBuffer);
}

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_info_bati11_opengles_myopengles_glapp_NDKApplication_initialize(
        JNIEnv* env,
        jobject _this
) {
    GLApp* app = sample1_initialize();

    jclass clazz = env->GetObjectClass(_this);
    jfieldID fid = env->GetFieldID(clazz, "glAppPtr", "J");
    env->SetLongField(_this, fid, (jlong)app);
}

JNIEXPORT void JNICALL Java_info_bati11_opengles_myopengles_glapp_NDKApplication_resized(
        JNIEnv* env,
        jobject _this,
        jint width,
        jint height
) {
    jclass clazz = env->GetObjectClass(_this);
    jfieldID fid = env->GetFieldID(clazz, "glAppPtr", "J");
    long long app = env->GetLongField(_this, fid);
    sample1_resized(reinterpret_cast<GLApp*>(app), width, height);
}

JNIEXPORT void JNICALL Java_info_bati11_opengles_myopengles_glapp_NDKApplication_rendering(
        JNIEnv* env,
        jobject _this,
        jint width,
        jint height
) {
    jclass clazz = env->GetObjectClass(_this);

    jenv = env;
    japp = _this;
    method_postFrontBuffer = env->GetMethodID(clazz, "postFrontBuffer", "()V");
//    save_method_postFrontBuffer_LocalRef(env, _this, env->GetMethodID(clazz, "postFrontBuffer", "()V"));

    jfieldID fid = env->GetFieldID(clazz, "glAppPtr", "J");
    long long app = env->GetLongField(_this, fid);
    sample1_rendering(reinterpret_cast<GLApp*>(app), width, height);
}


JNIEXPORT void JNICALL Java_info_bati11_opengles_myopengles_glapp_NDKApplication_destroy(
        JNIEnv* env,
        jobject _this
) {
    jclass clazz = env->GetObjectClass(_this);
    jfieldID fid = env->GetFieldID(clazz, "glAppPtr", "J");
    long long app = env->GetLongField(_this, fid);
    sample1_destroy(reinterpret_cast<GLApp*>(app));
}

#ifdef __cplusplus
}
#endif
