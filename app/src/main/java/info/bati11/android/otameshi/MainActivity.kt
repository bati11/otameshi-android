package info.bati11.android.otameshi

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import info.bati11.android.otameshi.gateway.GreetingService
import info.bati11.android.otameshi.ui.theme.OtameshiAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val uri by lazy { Uri.parse("http://10.0.2.2:8080") }
    private val greetingService by lazy { GreetingService(uri) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OtameshiAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting(
                        "Piyo",
                        greetingService = greetingService,
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    greetingService: GreetingService? = null,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var requestMessage by rememberSaveable { mutableStateOf("") }
    var responseMessage by rememberSaveable { mutableStateOf("") }
    Column(modifier = modifier) {
        Text(text = "My name is $name.")
        if (requestMessage != "") {
            Text(text = "$requestMessage ->")
        }
        if (responseMessage != "") {
            Text(text = "<- $responseMessage")
        }
        Button(
            onClick = {
                responseMessage = ""
                requestMessage = name
                scope.launch {
                    responseMessage = greetingService?.helloAndGet(name) ?: ""
                }
            },
        ) {
            Text("button")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OtameshiAppTheme {
        Greeting(
            "Android"
        )
    }
}