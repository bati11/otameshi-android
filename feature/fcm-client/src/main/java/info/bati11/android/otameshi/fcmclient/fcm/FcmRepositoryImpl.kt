package info.bati11.android.otameshi.fcmclient.fcm

import info.bati11.android.otameshi.fcmclient.domain.FcmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object FcmRepositoryImpl : FcmRepository {
    private val _fcmToken = MutableStateFlow("")
    override val fcmToken: StateFlow<String> = _fcmToken.asStateFlow()

    private val _message = MutableStateFlow("")
    override val message: StateFlow<String> = _message.asStateFlow()

    override suspend fun saveFcmToken(token: String) {
        _fcmToken.value = token
    }

    override suspend fun saveMessage(message: String) {
        _message.value = message
    }
}
