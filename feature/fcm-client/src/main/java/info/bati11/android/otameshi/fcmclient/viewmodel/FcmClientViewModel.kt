package info.bati11.android.otameshi.fcmclient.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import info.bati11.android.otameshi.fcmclient.domain.FcmRepository
import info.bati11.android.otameshi.fcmclient.fcm.FcmRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FcmClientViewModel(
    private val fcmRepository: FcmRepository = FcmRepositoryImpl,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    companion object {
        private val TAG = FcmClientViewModel::class.java.name
    }

    val fcmToken = fcmRepository.fcmToken
    val message = fcmRepository.message

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun initialize(applicationContext: Context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(applicationContext)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, null)

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            val token = task.result

            Log.i(TAG, "[FCM] new registration token: $token")
            viewModelScope.launch(dispatcher) {
                fcmRepository.saveFcmToken(token)
            }
        }
    }
}
