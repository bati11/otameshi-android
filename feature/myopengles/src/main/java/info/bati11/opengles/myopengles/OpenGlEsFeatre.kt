package info.bati11.opengles.myopengles

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val OPEN_GL_ES_FEATURE_ROUTE = "feature/opengles"

fun NavGraphBuilder.openGlEsFeatureNav() {
    composable(OPEN_GL_ES_FEATURE_ROUTE) {
        MyOpenGlEsApp()
    }
}
