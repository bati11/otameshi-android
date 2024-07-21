cmake_minimum_required(VERSION 3.22.1)

project("glapp")

add_library(${CMAKE_PROJECT_NAME} SHARED
        GLApp.h
        GLApp.cpp
        sample.h
        sample1.cpp
        NDKApplication.cpp
        )

target_link_libraries(${CMAKE_PROJECT_NAME}
        android
        log
        EGL
        GLESv2)
