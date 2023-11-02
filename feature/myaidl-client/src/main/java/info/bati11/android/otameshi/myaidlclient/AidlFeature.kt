package info.bati11.android.otameshi.myaidlclient

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val AIDL_FEATURE_ROUTE = "feature/aidl"

fun NavGraphBuilder.aidlFeatureNav() {
    composable(route = AIDL_FEATURE_ROUTE) {
        AidlApp()
    }
}
