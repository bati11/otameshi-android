package info.bati11.android.otameshi.fcmclient.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import info.bati11.android.otameshi.fcmclient.domain.FcmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val fcmRepository: FcmRepository = FcmRepositoryImpl

    companion object {
        private val TAG = MyFirebaseMessagingService::class.java.name
    }

    override fun onNewToken(token: String) {
        Log.i(TAG, "[FCM] Refreshed registration token: $token")
        scope.launch {
            fcmRepository.saveFcmToken(token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.i(TAG, "[FCM] onMessageReceived. messageId:${message.messageId} data:${message.data}")
        scope.launch {
            fcmRepository.saveMessage("from: ${message.from}, messageId: ${message.messageId}, data: ${message.data}")
        }
    }

    override fun onDeletedMessages() {
        Log.i(TAG, "[FCM] onDeletedMessages")
        super.onDeletedMessages()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
