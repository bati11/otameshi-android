package info.bati11.grpcclient.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import info.bati11.grpcclient.gateway.GreetingService
import kotlinx.coroutines.launch

@Composable
fun UnaryScreen(
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
