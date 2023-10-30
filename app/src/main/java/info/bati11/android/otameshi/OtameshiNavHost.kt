package info.bati11.android.otameshi

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import info.bati11.android.otameshi.ui.FeatureListScreen
import info.bati11.grpcclient.gateway.GreetingService
import info.bati11.grpcclient.grpcFeatureNav

@Composable
fun OtameshiNavHost(
    greetingService: GreetingService,
    navHostController: NavHostController,
    features: List<OtameshiDestination>,
    onSelectFeature: (OtameshiDestination) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navHostController,
        startDestination = TopDestination.route,
        modifier = modifier,
    ) {
        composable(route = TopDestination.route) {
            FeatureListScreen(
                features,
                onSelectedFeature = onSelectFeature,
            )
        }

        grpcFeatureNav(greetingService = greetingService)
    }
}
