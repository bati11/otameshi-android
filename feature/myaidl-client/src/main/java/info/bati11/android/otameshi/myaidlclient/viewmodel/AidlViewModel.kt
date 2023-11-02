package info.bati11.android.otameshi.myaidlclient.viewmodel

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.bati11.android.otameshi.myaidl.IRemoteService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class AidlViewModel : ViewModel() {
    private val isBinded = AtomicBoolean(false)
    private var remoteService: IRemoteService? = null

    companion object {
        private val TAG = AidlViewModel::class.java.simpleName
        private const val REMOTE_SERVICE_APP_PACKAGE = "info.bati11.android.otameshi.myaidlapp"
        private const val REMOTE_SERVICE_ACTION = "info.bati11.android.otameshi.myaidl.IRemoteService"
    }

    fun bindRemoteService(context: Context) {
        if (isBinded.compareAndSet(false, true)) {
            val intent = Intent(REMOTE_SERVICE_ACTION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
                intent.`package` = REMOTE_SERVICE_APP_PACKAGE
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    fun unbindRemoteService(context: Context) {
        if (isBinded.get()) {
            context.unbindService(connection)
            remoteService = null
            isBinded.set(false)
        }
    }

    fun getPid() {
        viewModelScope.launch(Dispatchers.IO) {
            remoteService?.getPid()?.let {
                Log.i(TAG, "getPid: $it")
            }
        }
    }

    val connection = object : ServiceConnection {

        override fun onBindingDied(name: ComponentName?) {
            Log.i(TAG, "onBindingDied")
            remoteService = null
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.i(TAG, "onServiceConnected")
            remoteService = IRemoteService.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i(TAG, "onServiceDisconnected")
            remoteService = null
        }

        override fun onNullBinding(name: ComponentName?) {
            Log.i(TAG, "onServiceDisconnected")
            remoteService = null
        }
    }
}
