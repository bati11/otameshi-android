#include "jni.h"
#include "./support.h"

void init_javavm(JNIEnv* env) {
    if (!g_javavm) {
        (*env)->GetJavaVM(env, &g_javavm);
    }
}

void set_clazz(JNIEnv* env, jobject obj) {
    if (g_clazz == NULL) {
        jclass clazz = (*env)->GetObjectClass(env, obj);
        g_clazz = (jclass)(*env)->NewGlobalRef(env, clazz);
    }
}

JNIEnv* currentJNIEnv() {
    JNIEnv *env;
    assert(g_javavm);
    (*g_javavm)->GetEnv(g_javavm, (void**) &env, JNI_VERSION_1_6);
    return env;
}

jmethodID JniApplication_methodID(JNIEnv* env, const char* method_name, const char* signeture) {
    return (*env)->GetMethodID(env, g_clazz, method_name, signeture);
}

jfieldID JniApplication_fieldID(JNIEnv* env, const char* field_name, const char* signeture) {
    return (*env)->GetFieldID(env, g_clazz, field_name, signeture);
}
