package info.bati11.grpcclient.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import info.bati11.grpcclient.gateway.GreetingService
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@Composable
fun BiStreamScreen(
    names: List<String>,
    greetingService: GreetingService,
    modifier: Modifier = Modifier
) {
    val responseMessage = greetingService.biStream.collectAsState(initial = "")
    var connectFailMessage = greetingService.biStreamError.collectAsState(initial = "")

    val scope = rememberCoroutineScope()
    var index by remember { mutableStateOf(0) }
    var messages by remember { mutableStateOf(listOf<String>()) }
    if (connectFailMessage.value != "") {
        Text("Failed to connect: ${connectFailMessage.value}")
    } else {
        Column() {
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
                        greetingService.helloBiDirection(name)
                    }
                },
            ) {
                Text("button")
            }
            Text(text = responseMessage.value)
        }
    }
}
