#ifndef OTAMESHIAPP_SUPPORT_H
#define OTAMESHIAPP_SUPPORT_H

#include "jni.h"
#include "../gl/GLApp.h"
#include "../gl/sample.h"

static JavaVM *g_javavm = NULL;
static jclass g_clazz = NULL;

void init_javavm(JNIEnv* env);
void set_clazz(JNIEnv* env, jobject obj);
JNIEnv* currentJNIEnv();
jmethodID JniApplication_methodID(JNIEnv* env, const char* method_name, const char* signeture);
jfieldID JniApplication_fieldID(JNIEnv* env, const char* field_name, const char* signeture);

#endif //OTAMESHIAPP_SUPPORT_H
