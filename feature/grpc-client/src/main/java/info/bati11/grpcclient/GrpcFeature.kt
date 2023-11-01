package info.bati11.grpcclient

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val GRPC_FEATURE_ROUTE = "feature/grpc"

fun NavGraphBuilder.grpcFeatureNav() {
    composable(route = GRPC_FEATURE_ROUTE) {
        GrpcClientApp()
    }
}
