package info.bati11.android.otameshi.myaidlapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import info.bati11.android.otameshi.myaidl.RemoteService
import info.bati11.android.otameshi.myaidlapp.theme.OtameshiAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OtameshiAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Row {
                        Button(onClick = {
                            val intent = Intent(applicationContext, RemoteService::class.java)
                            applicationContext.startForegroundService(intent)
                        }) {
                            Text("Start Foreground Service")
                        }
                        Button(onClick = {
                            val intent = Intent(applicationContext, RemoteService::class.java)
                            applicationContext.stopService(intent)
                        }) {
                            Text("Stop")
                        }
                    }
                }
            }
        }
    }
}
