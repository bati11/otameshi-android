package info.bati11.android.otameshi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.GoogleApiAvailability
import info.bati11.android.otameshi.common.ui.theme.OtameshiAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Google開発者サービスがインストールされていない場合は、インストールを促すダイアログを表示する
        GoogleApiAvailability().makeGooglePlayServicesAvailable(this)

        setContent {
            OtameshiAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    OtameshiApp()
                }
            }
        }
    }
}

@Composable
fun OtameshiApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentFeature =
        allFeatures.find { feature ->
            feature.route == currentDestination?.route
        } ?: Top

    val onSelectFeature = { selectedFeature: OtameshiFeature ->
        navController.navigate(selectedFeature.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        topBar = {
            TopBar(currentFeature.label, onClickAppLabel = { onSelectFeature(Top) })
        },
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            OtameshiNavHost(
                navHostController = navController,
                features = allFeatures.filter { it != Top },
                onSelectFeature = onSelectFeature,
            )
        }
    }
}

@Composable
internal fun TopBar(selectedFeature: String = "", onClickAppLabel: () -> Unit = {}) {
    Surface {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (selectedFeature == "") {
                Text(text = "TOP")
            } else {
                Text(text = selectedFeature)
            }
        }
        Box(
            modifier = Modifier
                .background(color = Color(0xFF0f5223))
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .clickable(enabled = true, onClick = onClickAppLabel),
        ) {
            Text(text = "OTAMESHI")
        }
    }
}
