package info.bati11.android.otameshi.gateway

import android.net.Uri
import android.util.Log
import info.bati11.android.otameshi.grpc.GreetingServiceGrpcKt
import info.bati11.android.otameshi.grpc.helloRequest
import io.grpc.*
import io.grpc.android.AndroidChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.withContext
import java.io.Closeable
import java.util.concurrent.TimeUnit

class GreetingService(private val uri: Uri) : Closeable {

    private val channel = let {
        Log.d(GreetingService::class.java.simpleName, "Connecting to ${uri.host}:${uri.port}")
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
                Log.e(GreetingService::class.java.simpleName, e.message, e)
                e.message ?: "error"
            }
        }
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}
