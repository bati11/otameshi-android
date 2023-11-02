package info.bati11.android.otameshi

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import info.bati11.android.otameshi.ui.FeatureListScreen
import info.bati11.android.otameshi.fcmclient.fcmFeatureNav
import info.bati11.grpcclient.grpcFeatureNav

@Composable
fun OtameshiNavHost(
    navHostController: NavHostController,
    features: List<OtameshiFeature>,
    onSelectFeature: (OtameshiFeature) -> Unit = {},
) {
    NavHost(
        navController = navHostController,
        startDestination = Top.route,
    ) {
        composable(route = Top.route) {
            FeatureListScreen(
                features,
                onSelectedFeature = onSelectFeature,
            )
        }

        grpcFeatureNav()
        fcmFeatureNav()
    }
}
