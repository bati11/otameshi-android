#include "./support.h"

void postFrontBuffer(GLApp* app) {
    JNIEnv *env = currentJNIEnv();
    jmethodID method_postFrontBuffer = JniApplication_methodID(env, "postFrontBuffer", "()V");
    jobject japp = app->japp;

    // フロントバッファへの反映をする
    // JNI経由で、Javaレイヤーの EGL10.eglSwapBuffers() を呼ぶ
    (*env)->CallVoidMethod(env, japp, method_postFrontBuffer);
}
