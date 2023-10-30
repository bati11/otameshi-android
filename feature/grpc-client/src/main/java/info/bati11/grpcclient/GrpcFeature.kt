package info.bati11.grpcclient

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import info.bati11.grpcclient.gateway.GreetingService

const val GRPC_FEATURE_ROUTE = "grpc_feature_route"

fun NavGraphBuilder.grpcFeatureNav(greetingService: GreetingService) {
    composable(route = GRPC_FEATURE_ROUTE) {
        GreetingApp(greetingService = greetingService)
    }
}
