#include <android/log.h>

#include "support.h"

#ifdef __cplusplus
extern "C" {
#endif

static JavaVM *g_javavm = NULL;

void ndk_support_initialize(JNIEnv *env) {
    if (!g_javavm) {
        (*env)->GetJavaVM(env, &g_javavm);
    }
}

static jclass ES20_class = NULL;
static jmethodID method_postFrontBuffer = NULL;

void ndk_loadClass(JNIEnv *env) {
    if (!ES20_class) {
        const char* class_signeture = "info/bati11/opengles/myopengles/glapp/NDKApplication";
        jclass result = (*env)->FindClass(env, class_signeture);
        if (!result) {
            __android_log_print(ANDROID_LOG_INFO, "glapp", "failed FindClass(%s)", class_signeture);
        }
        ES20_class = (*env)->NewGlobalRef(env, result);
    }
}

void ndk_loadMethod(JNIEnv *env) {
    const char* method_name = "postFrontBuffer";
    const char* method_signeture = "()V";
    jmethodID result = NULL;
    result = (*env)->GetStaticMethodID(env, ES20_class, method_name, method_signeture);

    if (!result) {
        __android_log_print(ANDROID_LOG_INFO, "glapp", "failed GetMethod(%s(%s)", method_name, method_signeture);
    }
    method_postFrontBuffer = result;
}


/**
 * 実行中スレッドに合ったJNIEnv*を取得する
 */
JNIEnv* ndk_current_JNIEnv() {
    JNIEnv *result = NULL;
    (*g_javavm)->GetEnv(g_javavm, (void**) &result, JNI_VERSION_1_6);
    return result;
}

void ES20_postFrontBuffer() {
    JNIEnv* env = ndk_current_JNIEnv();
    ndk_loadClass(env);
    ndk_loadMethod(env);
    (*env)->CallStaticVoidMethod(env, ES20_class, method_postFrontBuffer);
}

#ifdef __cplusplus
}
#endif
