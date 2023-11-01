package info.bati11.grpcclient.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import info.bati11.grpcclient.gateway.GreetingService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun ClientStreamScreen(
    names: List<String>,
    modifier: Modifier = Modifier,
) {
    var isConnected by remember { mutableStateOf(false) }
    var responseMessage by remember { mutableStateOf("") }
    var connectFailMessage by remember { mutableStateOf("") }
    GreetingService.response.collectAsState(Result.success("")).value.fold(
        onSuccess = { s -> responseMessage = s },
        onFailure = { e ->
            isConnected = false
            connectFailMessage = e.message ?: "connect error."
        },
    )
    val coroutineScope = rememberCoroutineScope()
    if (!isConnected) {
        LaunchedEffect(true) {
            GreetingService.connectClientStream(coroutineScope)
            delay(2000)
            isConnected = true
            connectFailMessage = ""
        }
        Text(
            if (connectFailMessage == "") {
                "connecting..."
            } else {
                "Failed to connect: $connectFailMessage"
            },
        )
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
                        GreetingService.hello(name)
                    }
                },
            ) {
                Text("button")
            }
            Text(text = responseMessage)
        }
    }
}
