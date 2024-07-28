#include "jni.h"
#include "../gl/GLApp.h"
#include "../gl/sample.h"

static JavaVM *g_javavm = NULL;
static jclass g_clazz = NULL;

void postFrontBuffer(GLApp* app) {
    JNIEnv *env;
    assert(g_javavm);
    (*g_javavm)->GetEnv(g_javavm, (void**) &env, JNI_VERSION_1_6);
    jmethodID method_postFrontBuffer = (*env)->GetMethodID(env, g_clazz, "postFrontBuffer", "()V");
    jobject japp = app->japp;

    // フロントバッファへの反映をする
    // JNI経由で、Javaレイヤーの EGL10.eglSwapBuffers() を呼ぶ
    (*env)->CallVoidMethod(env, japp, method_postFrontBuffer);
}

JNIEXPORT void JNICALL
Java_info_bati11_opengles_myopengles_glapp_jni_JniApplication_initializeNative(JNIEnv *env, jclass clazz) {
    if (!g_javavm) {
        (*env)->GetJavaVM(env, &g_javavm);
    }
}

JNIEXPORT void JNICALL Java_info_bati11_opengles_myopengles_glapp_jni_JniApplication_initialize(
        JNIEnv* env,
        jobject _this
) {
    GLApp* app = sample2_initialize();
    if (g_clazz == NULL) {
        jclass clazz = (*env)->GetObjectClass(env, _this);
        g_clazz = (jclass)(*env)->NewGlobalRef(env, clazz);
    }
    jfieldID fid = (*env)->GetFieldID(env, g_clazz, "glAppPtr", "J");
    (*env)->SetLongField(env, _this, fid, (jlong)app);
}

JNIEXPORT void JNICALL Java_info_bati11_opengles_myopengles_glapp_jni_JniApplication_resized(
        JNIEnv* env,
        jobject _this,
        jint width,
        jint height
) {
    jclass clazz = (*env)->GetObjectClass(env, _this);
    jfieldID fid = (*env)->GetFieldID(env, clazz, "glAppPtr", "J");
    long long app = (*env)->GetLongField(env, _this, fid);
    sample2_resized((GLApp*)(app), width, height);
}

JNIEXPORT void JNICALL Java_info_bati11_opengles_myopengles_glapp_jni_JniApplication_rendering(
        JNIEnv* env,
        jobject _this,
        jint width,
        jint height
) {
    if (g_clazz == NULL) {
        jclass clazz = (*env)->GetObjectClass(env, _this);
        g_clazz = (jclass)(*env)->NewGlobalRef(env, clazz);
    }
    jfieldID fid = (*env)->GetFieldID(env, g_clazz, "glAppPtr", "J");
    GLApp* app = (GLApp*)((*env)->GetLongField(env, _this, fid));
    app->japp = _this;
    sample2_rendering(app, width, height);
}


JNIEXPORT void JNICALL Java_info_bati11_opengles_myopengles_glapp_jni_JniApplication_destroy(
        JNIEnv* env,
        jobject _this
) {
    jclass clazz = (*env)->GetObjectClass(env, _this);
    jfieldID fid = (*env)->GetFieldID(env, clazz, "glAppPtr", "J");
    long long app = (*env)->GetLongField(env, _this, fid);
    sample2_destroy((GLApp*)(app));
}
