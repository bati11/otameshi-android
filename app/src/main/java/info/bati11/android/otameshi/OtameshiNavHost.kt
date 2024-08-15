package info.bati11.android.otameshi

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import info.bati11.android.otameshi.awsiotclient.awsIotFeature
import info.bati11.android.otameshi.ui.FeatureListScreen
import info.bati11.android.otameshi.fcmclient.fcmFeatureNav
import info.bati11.android.otameshi.myaidlclient.aidlFeatureNav
import info.bati11.grpcclient.grpcFeatureNav
import info.bati11.opengles.myopengles.openGlEsFeatureNav

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
        aidlFeatureNav()
        awsIotFeature()
        openGlEsFeatureNav()
    }
}
