package info.bati11.android.otameshi.grpc

import info.bati11.android.otameshi.grpc.GreetingServiceGrpc.getServiceDescriptor
import io.grpc.CallOptions
import io.grpc.CallOptions.DEFAULT
import io.grpc.Channel
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import io.grpc.ServerServiceDefinition
import io.grpc.ServerServiceDefinition.builder
import io.grpc.ServiceDescriptor
import io.grpc.Status
import io.grpc.Status.UNIMPLEMENTED
import io.grpc.StatusException
import io.grpc.kotlin.AbstractCoroutineServerImpl
import io.grpc.kotlin.AbstractCoroutineStub
import io.grpc.kotlin.ClientCalls.bidiStreamingRpc
import io.grpc.kotlin.ClientCalls.clientStreamingRpc
import io.grpc.kotlin.ClientCalls.serverStreamingRpc
import io.grpc.kotlin.ClientCalls.unaryRpc
import io.grpc.kotlin.ServerCalls.bidiStreamingServerMethodDefinition
import io.grpc.kotlin.ServerCalls.clientStreamingServerMethodDefinition
import io.grpc.kotlin.ServerCalls.serverStreamingServerMethodDefinition
import io.grpc.kotlin.ServerCalls.unaryServerMethodDefinition
import io.grpc.kotlin.StubFor
import kotlin.String
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import kotlinx.coroutines.flow.Flow

/**
 * Holder for Kotlin coroutine-based client and server APIs for myapp.GreetingService.
 */
object GreetingServiceGrpcKt {
  const val SERVICE_NAME: String = GreetingServiceGrpc.SERVICE_NAME

  @JvmStatic
  val serviceDescriptor: ServiceDescriptor
    get() = GreetingServiceGrpc.getServiceDescriptor()

  val helloMethod: MethodDescriptor<HelloRequest, HelloResponse>
    @JvmStatic
    get() = GreetingServiceGrpc.getHelloMethod()

  val helloServerStreamMethod: MethodDescriptor<HelloRequest, HelloResponse>
    @JvmStatic
    get() = GreetingServiceGrpc.getHelloServerStreamMethod()

  val helloClientStreamMethod: MethodDescriptor<HelloRequest, HelloResponse>
    @JvmStatic
    get() = GreetingServiceGrpc.getHelloClientStreamMethod()

  val helloBiStreamsMethod: MethodDescriptor<HelloRequest, HelloResponse>
    @JvmStatic
    get() = GreetingServiceGrpc.getHelloBiStreamsMethod()

  /**
   * A stub for issuing RPCs to a(n) myapp.GreetingService service as suspending coroutines.
   */
  @StubFor(GreetingServiceGrpc::class)
  class GreetingServiceCoroutineStub @JvmOverloads constructor(
    channel: Channel,
    callOptions: CallOptions = DEFAULT
  ) : AbstractCoroutineStub<GreetingServiceCoroutineStub>(channel, callOptions) {
    override fun build(channel: Channel, callOptions: CallOptions): GreetingServiceCoroutineStub =
        GreetingServiceCoroutineStub(channel, callOptions)

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    suspend fun hello(request: HelloRequest, headers: Metadata = Metadata()): HelloResponse =
        unaryRpc(
      channel,
      GreetingServiceGrpc.getHelloMethod(),
      request,
      callOptions,
      headers
    )
    /**
     * Returns a [Flow] that, when collected, executes this RPC and emits responses from the
     * server as they arrive.  That flow finishes normally if the server closes its response with
     * [`Status.OK`][Status], and fails by throwing a [StatusException] otherwise.  If
     * collecting the flow downstream fails exceptionally (including via cancellation), the RPC
     * is cancelled with that exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return A flow that, when collected, emits the responses from the server.
     */
    fun helloServerStream(request: HelloRequest, headers: Metadata = Metadata()):
        Flow<HelloResponse> = serverStreamingRpc(
      channel,
      GreetingServiceGrpc.getHelloServerStreamMethod(),
      request,
      callOptions,
      headers
    )
    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * This function collects the [Flow] of requests.  If the server terminates the RPC
     * for any reason before collection of requests is complete, the collection of requests
     * will be cancelled.  If the collection of requests completes exceptionally for any other
     * reason, the RPC will be cancelled for that reason and this method will throw that
     * exception.
     *
     * @param requests A [Flow] of request messages.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    suspend fun helloClientStream(requests: Flow<HelloRequest>, headers: Metadata = Metadata()):
        HelloResponse = clientStreamingRpc(
      channel,
      GreetingServiceGrpc.getHelloClientStreamMethod(),
      requests,
      callOptions,
      headers
    )
    /**
     * Returns a [Flow] that, when collected, executes this RPC and emits responses from the
     * server as they arrive.  That flow finishes normally if the server closes its response with
     * [`Status.OK`][Status], and fails by throwing a [StatusException] otherwise.  If
     * collecting the flow downstream fails exceptionally (including via cancellation), the RPC
     * is cancelled with that exception as a cause.
     *
     * The [Flow] of requests is collected once each time the [Flow] of responses is
     * collected. If collection of the [Flow] of responses completes normally or
     * exceptionally before collection of `requests` completes, the collection of
     * `requests` is cancelled.  If the collection of `requests` completes
     * exceptionally for any other reason, then the collection of the [Flow] of responses
     * completes exceptionally for the same reason and the RPC is cancelled with that reason.
     *
     * @param requests A [Flow] of request messages.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return A flow that, when collected, emits the responses from the server.
     */
    fun helloBiStreams(requests: Flow<HelloRequest>, headers: Metadata = Metadata()):
        Flow<HelloResponse> = bidiStreamingRpc(
      channel,
      GreetingServiceGrpc.getHelloBiStreamsMethod(),
      requests,
      callOptions,
      headers
    )}

  /**
   * Skeletal implementation of the myapp.GreetingService service based on Kotlin coroutines.
   */
  abstract class GreetingServiceCoroutineImplBase(
    coroutineContext: CoroutineContext = EmptyCoroutineContext
  ) : AbstractCoroutineServerImpl(coroutineContext) {
    /**
     * Returns the response to an RPC for myapp.GreetingService.Hello.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open suspend fun hello(request: HelloRequest): HelloResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method myapp.GreetingService.Hello is unimplemented"))

    /**
     * Returns a [Flow] of responses to an RPC for myapp.GreetingService.HelloServerStream.
     *
     * If creating or collecting the returned flow fails with a [StatusException], the RPC
     * will fail with the corresponding [Status].  If it fails with a
     * [java.util.concurrent.CancellationException], the RPC will fail with status
     * `Status.CANCELLED`.  If creating
     * or collecting the returned flow fails for any other reason, the RPC will fail with
     * `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open fun helloServerStream(request: HelloRequest): Flow<HelloResponse> = throw
        StatusException(UNIMPLEMENTED.withDescription("Method myapp.GreetingService.HelloServerStream is unimplemented"))

    /**
     * Returns the response to an RPC for myapp.GreetingService.HelloClientStream.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param requests A [Flow] of requests from the client.  This flow can be
     *        collected only once and throws [java.lang.IllegalStateException] on attempts to
     * collect
     *        it more than once.
     */
    open suspend fun helloClientStream(requests: Flow<HelloRequest>): HelloResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method myapp.GreetingService.HelloClientStream is unimplemented"))

    /**
     * Returns a [Flow] of responses to an RPC for myapp.GreetingService.HelloBiStreams.
     *
     * If creating or collecting the returned flow fails with a [StatusException], the RPC
     * will fail with the corresponding [Status].  If it fails with a
     * [java.util.concurrent.CancellationException], the RPC will fail with status
     * `Status.CANCELLED`.  If creating
     * or collecting the returned flow fails for any other reason, the RPC will fail with
     * `Status.UNKNOWN` with the exception as a cause.
     *
     * @param requests A [Flow] of requests from the client.  This flow can be
     *        collected only once and throws [java.lang.IllegalStateException] on attempts to
     * collect
     *        it more than once.
     */
    open fun helloBiStreams(requests: Flow<HelloRequest>): Flow<HelloResponse> = throw
        StatusException(UNIMPLEMENTED.withDescription("Method myapp.GreetingService.HelloBiStreams is unimplemented"))

    final override fun bindService(): ServerServiceDefinition = builder(getServiceDescriptor())
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = GreetingServiceGrpc.getHelloMethod(),
      implementation = ::hello
    ))
      .addMethod(serverStreamingServerMethodDefinition(
      context = this.context,
      descriptor = GreetingServiceGrpc.getHelloServerStreamMethod(),
      implementation = ::helloServerStream
    ))
      .addMethod(clientStreamingServerMethodDefinition(
      context = this.context,
      descriptor = GreetingServiceGrpc.getHelloClientStreamMethod(),
      implementation = ::helloClientStream
    ))
      .addMethod(bidiStreamingServerMethodDefinition(
      context = this.context,
      descriptor = GreetingServiceGrpc.getHelloBiStreamsMethod(),
      implementation = ::helloBiStreams
    )).build()
  }
}
