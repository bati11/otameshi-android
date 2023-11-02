package info.bati11.android.otameshi.fcmclient.domain

import kotlinx.coroutines.flow.StateFlow

interface FcmRepository {
    val fcmToken: StateFlow<String>
    val message: StateFlow<String>

    suspend fun saveFcmToken(token: String)
    suspend fun saveMessage(message: String)
}
