package info.bati11.android.otameshi.gateway

import android.net.Uri
import android.util.Log
import info.bati11.android.otameshi.grpc.GreetingServiceGrpcKt
import info.bati11.android.otameshi.grpc.HelloRequest
import info.bati11.android.otameshi.grpc.HelloResponse
import info.bati11.android.otameshi.grpc.helloRequest
import io.grpc.StatusException
import io.grpc.android.AndroidChannelBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import java.io.Closeable
import java.util.concurrent.TimeUnit

class GreetingService(
    private val uri: Uri,
    private val externalScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : Closeable {

    companion object {
        private val TAG = GreetingService.javaClass.simpleName
    }

    private val channel = let {
        Log.d(TAG, "Connecting to ${uri.host}:${uri.port}")
        val builder = AndroidChannelBuilder.forAddress(uri.host, uri.port)
        if (uri.scheme == "https") {
            builder.useTransportSecurity()
        } else {
            builder.usePlaintext()
        }
        builder.executor(Dispatchers.IO.asExecutor()).build()
    }

    private val stub = GreetingServiceGrpcKt.GreetingServiceCoroutineStub(channel)

    suspend fun helloAndGet(name: String): String {
        return withContext(Dispatchers.IO) {
            val request = helloRequest { this.name = name }
            try {
                val response = stub.hello(request)
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

    suspend fun connectClientStream() {
        withContext(ioDispatcher) {
            externalScope.launch {
                try {
                    val res = stub.helloClientStream(clientStream)
                    Log.i(TAG, "res: ${res.toString()}")
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

    fun connectBiStream(): Result<Flow<String>> {
        return try {
            val flow = stub.helloBiStreams(forBiStream).map { it.message }
            Result.success(flow)
        } catch (e: Throwable) {
            Result.failure(e)
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
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}
