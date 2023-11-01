package info.bati11.grpcclient

internal interface GrpcFeatureDestination {
    val label: String
    val route: String
}

internal object UnaryDestination : GrpcFeatureDestination {
    override val label = "Unary"
    override val route = "Unary"
}

internal object ClientStreamDestination : GrpcFeatureDestination {
    override val label = "ClientStream"
    override val route = "ClientStream"
}

internal object BiStreamDestination : GrpcFeatureDestination {
    override val label = "BiStream"
    override val route = "BiStream"
}

internal val allTabScreens = listOf(UnaryDestination, ClientStreamDestination, BiStreamDestination)
