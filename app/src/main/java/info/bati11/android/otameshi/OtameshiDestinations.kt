package info.bati11.android.otameshi

import info.bati11.grpcclient.GRPC_FEATURE_ROUTE

interface OtameshiDestination {
    val label: String
    val route: String
}

object TopDestination : OtameshiDestination {
    override val label = "Top"
    override val route = "Top"
}

object GrpcFeatureDestination : OtameshiDestination {
    override val label = "gRPC"
    override val route = GRPC_FEATURE_ROUTE
}

val allFeatures = listOf(TopDestination, GrpcFeatureDestination)
