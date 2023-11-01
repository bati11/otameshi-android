package info.bati11.grpcclient

import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import info.bati11.android.otameshi.common.ui.theme.OtameshiAppTheme
import info.bati11.grpcclient.gateway.GreetingService
import info.bati11.grpcclient.ui.GrpcAppTabRow

private val uri by lazy {
    // local pc loopback address
    Uri.parse("http://10.0.2.2:8080")
}

@Composable
fun GrpcClientApp() {
    OtameshiAppTheme {
        DisposableEffect(true) {
            GreetingService.initialize(uri)
            onDispose {
                GreetingService.close()
            }
        }

        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        var currentScreen =
            allTabScreens.find { it.route == currentDestination?.route } ?: UnaryDestination

        Scaffold(
            topBar = {
                GrpcAppTabRow(
                    allScreens = allTabScreens,
                    onTabSelected = { newScreen ->
                        navController.navigate(newScreen.route)
                    },
                    currentScreen = currentScreen,
                )
            },
        ) { innerPadding ->
            GrpcFeatureNavHost(
                navHostController = navController,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
