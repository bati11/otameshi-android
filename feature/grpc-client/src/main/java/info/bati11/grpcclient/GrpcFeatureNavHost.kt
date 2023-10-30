package info.bati11.grpcclient

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import info.bati11.grpcclient.gateway.GreetingService
import info.bati11.grpcclient.ui.BiStreamScreen
import info.bati11.grpcclient.ui.ClientStreamScreen
import info.bati11.grpcclient.ui.UnaryScreen

@Composable
fun GrpcFeatureNavHost(
    greetingService: GreetingService,
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navHostController,
        startDestination = UnaryDestination.route,
        modifier = modifier,
    ) {
        composable(route = UnaryDestination.route) {
            UnaryScreen(
                "Piyo",
                greetingService = greetingService,
            )
        }
        composable(route = ClientStreamDestination.route) {
            ClientStreamScreen(
                listOf("Piyo", "Foo", "Bar"),
                greetingService = greetingService,
            )
        }
        composable(route = BiStreamDestination.route) {
            BiStreamScreen(
                listOf("Piyo", "Foo", "Bar"),
                greetingService = greetingService,
            )
        }
    }
}
