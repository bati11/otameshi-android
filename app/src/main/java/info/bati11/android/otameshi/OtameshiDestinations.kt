package info.bati11.android.otameshi

interface OtameshiDestination {
    val label: String
    val route: String
}

object UnaryDestination : OtameshiDestination {
    override val label = "Unary"
    override val route = "Unary"
}

object ClientStreamDestination : OtameshiDestination {
    override val label = "ClientStream"
    override val route = "ClientStream"
}

object BiStreamDestination : OtameshiDestination {
    override val label = "BiStream"
    override val route = "BiStream"
}

val allTabScreens = listOf(UnaryDestination, ClientStreamDestination, BiStreamDestination)
