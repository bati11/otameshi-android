package info.bati11.grpcclient.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import info.bati11.grpcclient.gateway.GreetingService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ClientStreamScreen(
    names: List<String>,
    greetingService: GreetingService,
    modifier: Modifier = Modifier
) {
    var isConnected by remember { mutableStateOf(false) }
    var responseMessage by remember { mutableStateOf("") }
    var connectFailMessage by remember { mutableStateOf("") }
    greetingService.response.collectAsState(Result.success("")).value.fold(
        onSuccess = { s -> responseMessage = s },
        onFailure = { e ->
            isConnected = false
            connectFailMessage = e.message ?: "connect error."
        }
    )
    if (!isConnected) {
        LaunchedEffect(true) {
            greetingService.connectClientStream()
            delay(2000)
            isConnected = true
            connectFailMessage = ""
        }
        Text(if (connectFailMessage == "") {
            "connecting..."
        } else {
            "Failed to connect: $connectFailMessage"
        })
    } else {
        val scope = rememberCoroutineScope()
        var index by remember { mutableStateOf(0) }
        var messages by remember { mutableStateOf(listOf<String>()) }
        Column {
            Text("Success to connect.")
            LazyColumn {
                items(messages) { Text(text = it) }
            }
            Button(
                onClick = {
                    val name = names[index]
                    messages = messages + "$name ->"
                    index = if (index + 1 == names.size) 0 else index + 1
                    scope.launch {
                        greetingService.hello(name)
                    }
                },
            ) {
                Text("button")
            }
            Text(text = responseMessage)
        }
    }
}
