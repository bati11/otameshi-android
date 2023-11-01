package info.bati11.grpcclient.gateway

import android.net.Uri
import android.util.Log
import info.bati11.android.otameshi.grpc.GreetingServiceGrpcKt
import info.bati11.android.otameshi.grpc.HelloRequest
import info.bati11.android.otameshi.grpc.HelloResponse
import info.bati11.android.otameshi.grpc.helloRequest
import io.grpc.ManagedChannel
import io.grpc.StatusException
import io.grpc.android.AndroidChannelBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Closeable
import java.util.concurrent.TimeUnit

internal object GreetingService : Closeable {

    private val TAG = GreetingService::class.java.name

    private var channel: ManagedChannel? = null
    private var stub: GreetingServiceGrpcKt.GreetingServiceCoroutineStub? = null

    fun initialize(
        uri: Uri,
    ) {
        Log.d(TAG, "Connecting to ${uri.host}:${uri.port}")
//      val builder = ManagedChannelBuilder.forAddress(uri.host, uri.port)
        val builder = AndroidChannelBuilder.forAddress(uri.host, uri.port)
        if (uri.scheme == "https") {
            builder.useTransportSecurity()
        } else {
            builder.usePlaintext()
        }
//      builder.intercept(MyIntercepter())
        channel = builder.executor(Dispatchers.IO.asExecutor()).build()
        stub = GreetingServiceGrpcKt.GreetingServiceCoroutineStub(channel!!)
    }

//    class MyIntercepter: ClientInterceptor {
//        override fun <ReqT : Any?, RespT : Any?> interceptCall(
//            method: MethodDescriptor<ReqT, RespT>?,
//            callOptions: CallOptions?,
//            next: Channel?
//        ): ClientCall<ReqT, RespT> {
//            Log.i("INTERCEPTER", "before call. method:${method.toString()}, callOptions:${callOptions}")
//            val res = next!!.newCall(method, callOptions)
//            Log.i("INTERCEPTER", "after call. method:${method.toString()}, callOptions:${callOptions}")
//            return res
//        }
//    }
//

    suspend fun helloAndGet(name: String): String {
        return withContext(Dispatchers.IO) {
            val request = helloRequest { this.name = name }
            try {
                if (stub == null) throw IllegalStateException("stub is null.")
                val response = stub!!.hello(request)
                response.message
            } catch (e: StatusException) {
                Log.e(TAG, e.message, e)
                e.message ?: "error"
            }
        }
    }

    private val clientStream = MutableSharedFlow<HelloRequest>()
    private val _serverResponses = MutableSharedFlow<Result<HelloResponse>>()
    val response = _serverResponses.map { result ->
        result.map { it.message }
    }

    suspend fun connectClientStream(externalScope: CoroutineScope, ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {
        withContext(ioDispatcher) {
            externalScope.launch {
                try {
                    if (stub == null) throw IllegalStateException("stub is null.")
                    val res = stub!!.helloClientStream(clientStream)
                    Log.i(TAG, "res: $res")
                    _serverResponses.emit(Result.success(res))
                } catch (e: Throwable) {
                    Log.i(TAG, e.message, e)
                    _serverResponses.emit(Result.failure(e))
                }
            }
        }
    }

    suspend fun hello(name: String) {
        Log.i(TAG, "hello($name)")
        return withContext(Dispatchers.IO) {
            val request = helloRequest { this.name = name }
            clientStream.emit(request)
        }
    }

    private val forBiStream = MutableSharedFlow<HelloRequest>()
    private val _biStreamError = MutableSharedFlow<Throwable>()
    val biStreamError = _biStreamError.map { it.message ?: "connect error." }
    private val _biStream: MutableSharedFlow<String> = MutableSharedFlow()
    val biStream: Flow<String> = _biStream.asSharedFlow()

    suspend fun connectBiStream() {
        try {
            if (stub == null) throw IllegalStateException("stub is null.")
            stub!!.helloBiStreams(forBiStream)
                .catch { _biStreamError.emit(it) }
                .collect {
                    _biStream.emit(it.message)
                }
        } catch (e: Throwable) {
            Log.e(TAG, e.message, e)
            _biStreamError.emit(e)
        }
    }

    suspend fun helloBiDirection(name: String) {
        Log.i(TAG, "helloBi($name)")
        return withContext(Dispatchers.IO) {
            val request = helloRequest { this.name = name }
            forBiStream.emit(request)
        }
    }

    override fun close() {
        Log.i(TAG, "close")
        channel?.shutdown()?.awaitTermination(5, TimeUnit.SECONDS)
        channel = null
        stub = null
    }
}
