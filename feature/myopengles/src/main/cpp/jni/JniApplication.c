#include "jni.h"
#include "./support.h"

const int sample_number = 5;

JNIEXPORT void JNICALL
Java_info_bati11_opengles_myopengles_glapp_jni_JniApplication_initializeNative(JNIEnv *env, jclass clazz) {
    init_javavm(env);
}

JNIEXPORT void JNICALL Java_info_bati11_opengles_myopengles_glapp_jni_JniApplication_initialize(
        JNIEnv* env,
        jobject _this
) {
    set_clazz(env, _this);
    GLApp *app = (GLApp*)malloc(sizeof(GLApp));
    app->japp = _this;

    switch(sample_number) {
        case 1:
            sample1_initialize((GLApp*)(app));
            break;
        case 2:
            sample2_initialize((GLApp*)(app));
            break;
        case 3:
            sample3_initialize((GLApp*)(app));
            break;
        case 4:
            sample4_initialize((GLApp*)(app));
            break;
        case 5:
            sample5_initialize((GLApp*)(app));
            break;
    }

    jfieldID fid = JniApplication_fieldID(env, "glAppPtr", "J");
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

    switch(sample_number) {
        case 1:
            sample1_resized((GLApp*)(app), width, height);
            break;
        case 2:
            sample2_resized((GLApp*)(app), width, height);
            break;
        case 3:
            sample3_resized((GLApp*)(app), width, height);
            break;
        case 4:
            sample4_resized((GLApp*)(app), width, height);
            break;
        case 5:
            sample5_resized((GLApp*)(app), width, height);
            break;
    }
}

JNIEXPORT void JNICALL Java_info_bati11_opengles_myopengles_glapp_jni_JniApplication_rendering(
        JNIEnv* env,
        jobject _this,
        jint width,
        jint height
) {
    set_clazz(env, _this);
    jfieldID fid = JniApplication_fieldID(env, "glAppPtr", "J");
    GLApp* app = (GLApp*)((*env)->GetLongField(env, _this, fid));
    app->japp = _this;
    switch(sample_number) {
        case 1:
            sample1_rendering((GLApp*)(app), width, height);
            break;
        case 2:
            sample2_rendering((GLApp*)(app), width, height);
            break;
        case 3:
            sample3_rendering((GLApp*)(app), width, height);
            break;
        case 4:
            sample4_rendering((GLApp*)(app), width, height);
            break;
        case 5:
            sample5_rendering((GLApp*)(app), width, height);
            break;
    }
}


JNIEXPORT void JNICALL Java_info_bati11_opengles_myopengles_glapp_jni_JniApplication_destroy(
        JNIEnv* env,
        jobject _this
) {
    jclass clazz = (*env)->GetObjectClass(env, _this);
    jfieldID fid = (*env)->GetFieldID(env, clazz, "glAppPtr", "J");
    long long app = (*env)->GetLongField(env, _this, fid);
    switch(sample_number) {
        case 1:
            sample1_destroy((GLApp*)(app));
            break;
        case 2:
            sample2_destroy((GLApp*)(app));
            break;
        case 3:
            sample3_destroy((GLApp*)(app));
            break;
        case 4:
            sample4_destroy((GLApp*)(app));
            break;
        case 5:
            sample5_destroy((GLApp*)(app));
            break;
    }
}
