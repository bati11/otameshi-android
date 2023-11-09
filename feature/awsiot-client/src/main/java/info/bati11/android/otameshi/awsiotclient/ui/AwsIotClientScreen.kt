package info.bati11.android.otameshi.awsiotclient.ui

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import info.bati11.android.otameshi.awsiotclient.service.AwsIotClientServiceWithAwsAndroidSdkIot
import info.bati11.android.otameshi.awsiotclient.service.AwsIotClientServiceWithAwsIotDeviceSdkJavaV2

@Composable
internal fun AwsIotClientScreen(
    applicationContext: Context = LocalContext.current,
) {
    val mqttClientId = Settings.Secure.getString(
        applicationContext.contentResolver,
        Settings.Secure.ANDROID_ID,
    )
    Column {
        Text(text = "aws-iot-device-sdk-java-v2")
        Button(onClick = {
            val intent = Intent(applicationContext, AwsIotClientServiceWithAwsIotDeviceSdkJavaV2::class.java)
            intent.putExtra("mqttClientId", mqttClientId)
            applicationContext.startForegroundService(intent)
        }) {
            Text("Start AwsIotClient Service")
        }
        Button(onClick = {
            val intent = Intent(applicationContext, AwsIotClientServiceWithAwsIotDeviceSdkJavaV2::class.java)
            applicationContext.stopService(intent)
        }) {
            Text("Stop Service")
        }
        Text(text = "aws-sdk-android")
        Button(onClick = {
            val intent = Intent(applicationContext, AwsIotClientServiceWithAwsAndroidSdkIot::class.java)
            intent.putExtra("mqttClientId", mqttClientId)
            applicationContext.startForegroundService(intent)
        }) {
            Text("Start AwsIotClient Service")
        }
        Button(onClick = {
            val intent = Intent(applicationContext, AwsIotClientServiceWithAwsAndroidSdkIot::class.java)
            applicationContext.stopService(intent)
        }) {
            Text("Stop Service")
        }
    }
}
