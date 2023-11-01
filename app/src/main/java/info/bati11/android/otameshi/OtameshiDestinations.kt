package info.bati11.android.otameshi

import info.bati11.grpcclient.GRPC_FEATURE_ROUTE

interface OtameshiFeature {
    val label: String
    val route: String
}

object Top : OtameshiFeature {
    override val label = "Top"
    override val route = "Top"
}

object GrpcFeature : OtameshiFeature {
    override val label = "gRPC"
    override val route = GRPC_FEATURE_ROUTE
}

val allFeatures = listOf(Top, GrpcFeature)
