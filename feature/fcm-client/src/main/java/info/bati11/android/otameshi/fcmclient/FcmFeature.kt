package info.bati11.android.otameshi.fcmclient

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val FCM_FEATURE_ROUTE = "feature/fcm"

fun NavGraphBuilder.fcmFeatureNav() {
    composable(route = FCM_FEATURE_ROUTE) {
        FcmClientApp()
    }
}
