#include <string>
#include <android/log.h>

#include "support.h"
#include "sample.h"

void sample1_initialize() {
    __android_log_print(ANDROID_LOG_INFO, "glapp", "initialize");
}

void sample1_resized(int width, int height) {
    __android_log_print(ANDROID_LOG_INFO, "glapp", "resized() width:%d, height:%d", width, height);
}

void sample1_rendering(int width, int height) {
    __android_log_print(ANDROID_LOG_INFO, "glapp", "rendering() width:%d, height:%d", width, height);

    glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);
    ES20_postFrontBuffer();
}

void sample1_destroy() {
    __android_log_print(ANDROID_LOG_INFO, "glapp", "dhhestroy");
}
