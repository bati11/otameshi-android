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
import com.amazonaws.mobileconnectors.iot.AWSIotKeystoreHelper
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos
import com.amazonaws.util.IOUtils
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory
import java.security.KeyStore

/**
 * https://github.com/aws-amplify/aws-sdk-android
 */
internal class AwsIotClientServiceWithAwsAndroidSdkIot : Service() {

    companion object {
        private val TAG = AwsIotClientServiceWithAwsAndroidSdkIot::class.java.simpleName
        private const val SERVICE_ID = 512
        private const val CHANNEL_ID = "awsiotclient.service.AwsIotClientService"

        private const val AWSIOT_CERT_ID = "default_cert_id"
        private const val AWSIOT_KEY_STORE_NAME = "default.jks"
        private const val AWSIOT_DEFAULT_PASSWORD = "default_password"
        private const val CRT_FILE_PATH = "aws.iot/certificate.pem.crt"
        private const val PRIVATE_KEY_FILE_PATH = "aws.iot/private.pem.key"

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

        start("test-$mqttClientId")
        return START_NOT_STICKY
    }

    inner class LocalBinder : Binder() {
        fun getService(): AwsIotClientServiceWithAwsAndroidSdkIot = this@AwsIotClientServiceWithAwsAndroidSdkIot
    }

    private val binder = LocalBinder()

    override fun onBind(p0: Intent?): IBinder? {
        Log.i(TAG, "onBind()")
        return binder
    }

    private var isSubscribed = false
    private var mqttManager: AWSIotMqttManager? = null

    private fun start(mqttClientId: String) {
        connectionClose()
        LoggerFactory.setLogger(MqttLogger::class.java.name)

        val keyStore = getKeyStore(this)
        mqttManager = AWSIotMqttManager(mqttClientId, AWSIOT_ENDPOINT)
        try {
            mqttManager?.connect(keyStore) { status, throwable ->
                if (throwable != null) {
                    Log.e(TAG, "Connection callback error", throwable)
                    throw throwable
                }
                Log.d(TAG, "AWSIotMqttClientStatus changed.($status)")
                if (status == AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connected) {
                    mqttManager?.subscribeToTopic(TOPIC_NAME, AWSIotMqttQos.QOS1) { topic, data ->
                        Log.i(TAG, "onMessageArrived. data:${String(data, Charsets.UTF_8)}")
                    }
                    isSubscribed = true
                }
            }
            Log.i(TAG, "After mqttManager?.connect()")
        } catch (e: Exception) {
            Log.e(TAG, "Connection error.", e)
            throw e
        }

        startForeground()
    }

    private fun getKeyStore(context: Context): KeyStore {
        val keystorePath = context.filesDir.absolutePath
        val crt = IOUtils.toString(context.resources.assets.open(CRT_FILE_PATH))
        val privateKey = IOUtils.toString(context.resources.assets.open(PRIVATE_KEY_FILE_PATH))
        if (AWSIotKeystoreHelper.isKeystorePresent(keystorePath, AWSIOT_KEY_STORE_NAME)) {
            AWSIotKeystoreHelper.deleteKeystoreAlias(
                AWSIOT_CERT_ID,
                keystorePath,
                AWSIOT_KEY_STORE_NAME,
                AWSIOT_DEFAULT_PASSWORD,
            )
        }
        AWSIotKeystoreHelper.saveCertificateAndPrivateKey(
            AWSIOT_CERT_ID,
            crt,
            privateKey,
            keystorePath,
            AWSIOT_KEY_STORE_NAME,
            AWSIOT_DEFAULT_PASSWORD,
        )

        return AWSIotKeystoreHelper.getIotKeystore(
            AWSIOT_CERT_ID,
            keystorePath,
            AWSIOT_KEY_STORE_NAME,
            AWSIOT_DEFAULT_PASSWORD,
        )
    }

    private fun connectionClose() {
        Log.i(TAG, "connectionClose()")
        if (mqttManager != null) {
            try {
                mqttManager?.disconnect()
            } finally {
                mqttManager = null
            }
        }
        isSubscribed = false
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
