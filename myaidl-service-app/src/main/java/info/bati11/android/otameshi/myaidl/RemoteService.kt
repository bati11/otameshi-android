package info.bati11.android.otameshi.myaidl

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.Process
import android.util.Log
import androidx.core.app.NotificationCompat

class RemoteService : Service() {

    companion object {
        private val TAG = RemoteService::class.java.simpleName
        private const val SERVICE_ID = 100
        private const val CHANNEL_ID = "myaidlservice.app.RemoteService"
    }

    override fun onCreate() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (manager.getNotificationChannel(CHANNEL_ID) == null) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "MyAidlService用の通知エリア",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            manager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        start()
        return START_NOT_STICKY
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("MyAidlService")
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

    private val binder = object : IRemoteService.Stub() {

        override fun getPid(): Int {
            val pid = Process.myPid()
            Log.i(TAG, "getPid(): $pid")
            return pid
        }

        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String?,
        ) {
            Log.i(TAG, "basicTypes(): $anInt, $aLong, $aBoolean, $aFloat, $aDouble, $aString")
        }
    }

    override fun onBind(intent: Intent): IBinder {
        Log.i(TAG, "onBind()")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.i(TAG, "onUnbind()")
        stopSelf()
        return true
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy()")
    }
}
