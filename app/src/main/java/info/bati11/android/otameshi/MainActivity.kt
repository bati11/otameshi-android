package info.bati11.android.otameshi

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import info.bati11.android.otameshi.gateway.GreetingService
import info.bati11.android.otameshi.ui.OtameshiTabRow
import info.bati11.android.otameshi.ui.theme.OtameshiAppTheme

class MainActivity : ComponentActivity() {

    private val uri by lazy { Uri.parse("http://10.0.2.2:8080") }
    private val greetingService by lazy { GreetingService(uri) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GreetingApp(greetingService)
        }
    }
}

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
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            OtameshiNavHost(
                greetingService = greetingService,
                navHostController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
