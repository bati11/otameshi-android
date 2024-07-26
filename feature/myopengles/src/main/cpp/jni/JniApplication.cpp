#include <jni.h>
#include "../gl/GLApp.h"
#include "../gl/sample.h"

struct JniApp : GLApp {
    jobject japp;
    JniApp(jobject japp, GLApp* glApp) : GLApp()
    {
        JniApp::japp = japp;
        JniApp::shader_program = glApp->frag_shader;
        JniApp::vert_shader = glApp->vert_shader;
        JniApp::frag_shader = glApp->frag_shader;
        JniApp::attr_pos = glApp->attr_pos;
    }
};

static JavaVM *g_javavm = NULL;
static jclass g_clazz = NULL;

void postFrontBuffer(GLApp* app) {
    JNIEnv* env;
    g_javavm->GetEnv((void**) &env, JNI_VERSION_1_6);
    jmethodID method_postFrontBuffer = env->GetMethodID(g_clazz, "postFrontBuffer", "()V");
    jobject japp = ((JniApp*)app)->japp;
    env->CallVoidMethod(japp, method_postFrontBuffer);
}

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL
Java_info_bati11_opengles_myopengles_glapp_jni_JniApplication_initializeNative(JNIEnv *env, jclass clazz) {
    if (!g_javavm) {
        env->GetJavaVM(&g_javavm);
    }
}

JNIEXPORT void JNICALL Java_info_bati11_opengles_myopengles_glapp_jni_JniApplication_initialize(
        JNIEnv* env,
        jobject _this
) {
    GLApp* app = sample1_initialize();

    jclass clazz = env->GetObjectClass(_this);
    jfieldID fid = env->GetFieldID(clazz, "glAppPtr", "J");
    env->SetLongField(_this, fid, (jlong)app);
}

JNIEXPORT void JNICALL Java_info_bati11_opengles_myopengles_glapp_jni_JniApplication_resized(
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

JNIEXPORT void JNICALL Java_info_bati11_opengles_myopengles_glapp_jni_JniApplication_rendering(
        JNIEnv* env,
        jobject _this,
        jint width,
        jint height
) {
    jclass _clazz = env->GetObjectClass(_this);
    if (g_clazz == NULL) {
        g_clazz = (jclass)env->NewGlobalRef(_clazz);
    }
    jfieldID fid = env->GetFieldID(_clazz, "glAppPtr", "J");
    auto gapp = reinterpret_cast<GLApp*>(env->GetLongField(_this, fid));
    JniApp app(_this, gapp);
    sample1_rendering(&app, width, height);
}


JNIEXPORT void JNICALL Java_info_bati11_opengles_myopengles_glapp_jni_JniApplication_destroy(
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
