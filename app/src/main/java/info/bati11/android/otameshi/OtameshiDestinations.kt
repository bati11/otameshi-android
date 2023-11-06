package info.bati11.android.otameshi

import info.bati11.android.otameshi.fcmclient.FCM_FEATURE_ROUTE
import info.bati11.android.otameshi.myaidlclient.AIDL_FEATURE_ROUTE
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

object FcmFeature : OtameshiFeature {
    override val label = "FCM"
    override val route = FCM_FEATURE_ROUTE
}

object AidlFeature : OtameshiFeature {
    override val label = "AIDL"
    override val route = AIDL_FEATURE_ROUTE
}

val allFeatures = listOf(Top, GrpcFeature, FcmFeature, AidlFeature)
