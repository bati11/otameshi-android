cmake_minimum_required(VERSION 3.22.1)

project("glapp")

add_library(${CMAKE_PROJECT_NAME} SHARED
        gl/GLApp.h
        gl/sample_helper.h
        gl/sample.h
        gl/sample_helper.c
        gl/sample1.c
        gl/sample2.c
        jni/JniApplication.c
        )

target_link_libraries(${CMAKE_PROJECT_NAME}
        android
        log
        EGL
        GLESv2)
