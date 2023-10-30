package info.bati11.grpcclient

interface GrpcFeatureDestination {
    val label: String
    val route: String
}

object UnaryDestination : GrpcFeatureDestination {
    override val label = "Unary"
    override val route = "Unary"
}

object ClientStreamDestination : GrpcFeatureDestination {
    override val label = "ClientStream"
    override val route = "ClientStream"
}

object BiStreamDestination : GrpcFeatureDestination {
    override val label = "BiStream"
    override val route = "BiStream"
}

val allTabScreens = listOf(UnaryDestination, ClientStreamDestination, BiStreamDestination)
