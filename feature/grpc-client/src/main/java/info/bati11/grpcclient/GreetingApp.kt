package info.bati11.grpcclient

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import info.bati11.grpcclient.gateway.GreetingService
import info.bati11.grpcclient.ui.OtameshiTabRow
import info.bati11.grpcclient.ui.theme.OtameshiAppTheme

@Composable
fun GreetingApp(
    greetingService: GreetingService
) {
    OtameshiAppTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        var currentScreen =
            allTabScreens.find { it.route == currentDestination?.route } ?: UnaryDestination

        Scaffold(
            topBar = {
                OtameshiTabRow(
                    allScreens = allTabScreens,
                    onTabSelected = { newScreen ->
                        navController.navigate(newScreen.route)
                    },
                    currentScreen = currentScreen,
                )
            },
        ) { innerPadding ->
            GrpcFeatureNavHost(
                greetingService = greetingService,
                navHostController = navController,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
