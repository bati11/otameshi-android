cmake_minimum_required(VERSION 3.22.1)

project("glapp")

add_library(${CMAKE_PROJECT_NAME} SHARED
        gl/GLApp.h
        gl/GLApp.cpp
        gl/sample.h
        gl/sample_helper.cpp
        gl/sample1.cpp
        jni/JniApplication.cpp
        )

target_link_libraries(${CMAKE_PROJECT_NAME}
        android
        log
        EGL
        GLESv2)
