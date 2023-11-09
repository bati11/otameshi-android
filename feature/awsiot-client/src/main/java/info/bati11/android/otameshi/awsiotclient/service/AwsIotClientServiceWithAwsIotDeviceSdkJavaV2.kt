package info.bati11.android.otameshi.awsiotclient.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import software.amazon.awssdk.crt.CrtResource
import software.amazon.awssdk.crt.mqtt.MqttClientConnection
import software.amazon.awssdk.crt.mqtt.MqttClientConnectionEvents
import software.amazon.awssdk.crt.mqtt.QualityOfService
import software.amazon.awssdk.iot.AwsIotMqttConnectionBuilder

/**
 * https://github.com/aws/aws-iot-device-sdk-java-v2
 */
class AwsIotClientServiceWithAwsIotDeviceSdkJavaV2 : Service() {

    companion object {
        private val TAG = AwsIotClientServiceWithAwsIotDeviceSdkJavaV2::class.java.simpleName
        private const val SERVICE_ID = 338
        private const val CHANNEL_ID = "awsiotclient.service.AwsIotClientServiceWith"

        private const val CRT_FILE_PATH = "aws.iot/certificate.pem.crt"
        private const val PRIVATE_KEY_FILE_PATH = "aws.iot/private.pem.key"
        private const val CA_FILE_PATH = "aws.iot/AmazonRootCA1.pem"

        private const val AWSIOT_ENDPOINT = "xxxxxxxxxx-ats.iot.us-east-1.amazonaws.com"
        private const val TOPIC_NAME = "test/topic"
    }

    override fun onCreate() {
        Log.i(TAG, "onCreate()")
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (manager.getNotificationChannel(CHANNEL_ID) == null) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "AwsIotClientService用の通知エリア",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            manager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand()")
        val mqttClientIdSuffix = intent?.getStringExtra("mqttClientId") ?: System.currentTimeMillis().toString()

        // 一意である必要がある、かつ、AWS IoT ポリシーのclientの指定とマッチする必要がある
        val mqttClientId = "test-$mqttClientIdSuffix"

        start(mqttClientId)
        return START_NOT_STICKY
    }

    inner class LocalBinder : Binder() {
        fun getService(): AwsIotClientServiceWithAwsIotDeviceSdkJavaV2 = this@AwsIotClientServiceWithAwsIotDeviceSdkJavaV2
    }

    private val binder = LocalBinder()

    override fun onBind(p0: Intent?): IBinder? {
        Log.i(TAG, "onBind()")
        return binder
    }

    private var isSubscribed = false
    private var mqttClientConnection: MqttClientConnection? = null

    private fun start(mqttClientId: String) {
        connectionClose()

        val callback = object : MqttClientConnectionEvents {
            override fun onConnectionInterrupted(errorCode: Int) {
                if (errorCode != 0) {
                    Log.e(TAG, "Connection interrupted. errorCode: $errorCode")
                }
            }

            override fun onConnectionResumed(sessionPresent: Boolean) {
                Log.i(TAG, "Connection resumed: ${ if (sessionPresent) "existing session" else "clean session" })")
            }
        }

        val crt = resources.assets.open(CRT_FILE_PATH).readBytes()
        val privateKey = resources.assets.open(PRIVATE_KEY_FILE_PATH).readBytes()
        val ca = resources.assets.open(CA_FILE_PATH).readBytes()

        try {
            val builder = AwsIotMqttConnectionBuilder.newMtlsBuilder(crt, privateKey)
            builder.withCertificateAuthority(String(ca, Charsets.UTF_8))

            builder.withConnectionEventCallbacks(callback)
                .withClientId(mqttClientId)
                .withEndpoint(AWSIOT_ENDPOINT)
                .withPort(8883)
                .withCleanSession(true)
                .withProtocolOperationTimeoutMs(30000)

            mqttClientConnection = builder.build()
            builder.close()

            // Connect the MQTT client
            val connected = mqttClientConnection?.connect()
            try {
                val sessionPresent = connected?.get()
                Log.i(TAG, "Connected to " + (if (sessionPresent == false) "new" else "existing") + " session!")
            } catch (e: Exception) {
                Log.e(TAG, "Error connecting to AWS IoT Core.", e)
                throw e
            }

            // Subscribe to the MQTT topic
            val subscribed = mqttClientConnection?.subscribe(TOPIC_NAME, QualityOfService.AT_LEAST_ONCE) { message ->
                Log.i(TAG, "onMessageArrived message: ${String(message.payload, Charsets.UTF_8)}")
            }
            subscribed?.get()
            isSubscribed = true
        } catch (e: Exception) {
            Log.e(TAG, "Connection error.", e)
            throw e
        }

        startForeground()
    }

    private fun connectionClose() {
        Log.i(TAG, "connectionClose()")
        if (isSubscribed) {
            mqttClientConnection?.disconnect()
            mqttClientConnection?.close()
        }
        isSubscribed = false
        CrtResource.waitForNoResources()
    }

    private fun startForeground() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("AwsIotClientService")
            .setContentText("runnging...")
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                SERVICE_ID,
                notification,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                } else {
                    0
                },
            )
        } else {
            startForeground(SERVICE_ID, notification)
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.i(TAG, "onUnBind()")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy()")
        connectionClose()
    }
}
