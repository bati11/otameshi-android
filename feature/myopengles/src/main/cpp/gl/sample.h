#ifndef OTAMESHIAPP_SAMPLE_H
#define OTAMESHIAPP_SAMPLE_H

#include "sample_helper.h"

void postFrontBuffer(GLApp* app);

GLApp* sample1_initialize();
void sample1_resized(GLApp* app, int width, int height);
void sample1_rendering(GLApp* app, int width, int height);
void sample1_destroy(GLApp* app);

GLApp* sample2_initialize();
void sample2_resized(GLApp* app, int width, int height);
void sample2_rendering(GLApp* app, int width, int height);
void sample2_destroy(GLApp* app);

#endif //OTAMESHIAPP_SAMPLE_H
