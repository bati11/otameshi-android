package info.bati11.android.otameshi.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import info.bati11.android.otameshi.gateway.GreetingService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@Composable
fun BiStreamScreen(
    names: List<String>,
    greetingService: GreetingService,
    modifier: Modifier = Modifier
) {
    var isConnected by remember { mutableStateOf(false) }
    val connectResult = greetingService.connectBiStream()
    var responseMessage = connectResult.fold(
        onSuccess = { it.collectAsState(initial = "") },
        onFailure = { mutableStateOf("") }
    )
    var connectFailMessage = connectResult.fold(
        onSuccess = { mutableStateOf("") },
        onFailure = { mutableStateOf(it.message ?: "connect error.") }
    )

    val scope = rememberCoroutineScope()
    var index by remember { mutableStateOf(0) }
    var messages by remember { mutableStateOf(listOf<String>()) }
    if (!isConnected) {
        LaunchedEffect(true) {
            greetingService.connectClientStream()
            delay(2000)
            isConnected = true
            connectFailMessage.value = ""
        }
        Text(if (connectFailMessage.value == "") {
            "connecting..."
        } else {
            "Failed to connect: $connectFailMessage.value"
        })
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
