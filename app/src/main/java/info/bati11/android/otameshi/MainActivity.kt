package info.bati11.android.otameshi

import android.net.Uri
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import info.bati11.android.otameshi.ui.theme.OtameshiAppTheme
import info.bati11.grpcclient.gateway.GreetingService
import kotlinx.coroutines.GlobalScope

class MainActivity : ComponentActivity() {

    companion object {
        private val TAG = MainActivity::class.java.name
    }

    private val appScope = GlobalScope
    private val uri by lazy { Uri.parse("http://10.0.2.2:8080") }
    private val greetingService by lazy {
        GreetingService(
            uri,
            externalScope = appScope,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OtameshiAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GreetingApp(greetingService)
                }
            }
        }
    }
}

@Composable
fun GreetingApp(greetingService: GreetingService) {
    val navController = rememberNavController()

    var headerLabel by remember { mutableStateOf("") }

    val onSelectFeature = { selectedFeature: OtameshiDestination ->
        var nextScreen =
            allFeatures.find { feature ->
                feature == selectedFeature
            } ?: TopDestination
        navController.navigate(nextScreen.route)
        headerLabel = nextScreen.label
    }

    Scaffold(
        topBar = {
            TopBar(headerLabel, onClickAppLabel = { onSelectFeature(TopDestination) })
        },
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            OtameshiNavHost(
                navHostController = navController,
                features = allFeatures.filter { it != TopDestination },
                onSelectFeature = onSelectFeature,
                greetingService = greetingService,
            )
        }
    }
}

@Composable
fun TopBar(selectedFeature: String = "", onClickAppLabel: () -> Unit = {}) {
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
