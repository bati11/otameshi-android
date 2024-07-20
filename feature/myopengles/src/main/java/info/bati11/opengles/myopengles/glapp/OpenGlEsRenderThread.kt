package info.bati11.opengles.myopengles.glapp

import android.graphics.PixelFormat
import android.view.SurfaceHolder
import android.view.SurfaceView
import info.bati11.opengles.myopengles.glapp.egl.EGLDevice
import info.bati11.opengles.myopengles.glapp.egl.EGLManager

/**
 * OpenGL ES 描画用スレッド
 *
 * OpenGL ES の状態はEGLContextに保存される。
 * 1つのスレッドにつき、1つのEGLContextしかバインドされない。
 * EGLContextは、EGLSurfaceとペアでスレッドにバインドされる。
 * EGLSurfaceは描画対象のメモリを抽象化したもの。
 *
 * EGLContextとEGLSurfaceのペアをEGLDeviceに保持する実装とする。
 */
class OpenGlEsRenderThread(private val app: GLApplication): Thread() {

    private lateinit var eglManager: EGLManager
    var windowDevice: EGLDevice? = null
        private set

    private var status: Status = Status.RUN

    private enum class Status {
        RUN,
        PAUSE,
        DESTROY,
        ;
    }

    fun initialize(surfaceView: SurfaceView) {
        // OpenGL ESの描画に必要なのは SurfaceHolder. SurfaceView ではない.
        val surfaceHolder = surfaceView.holder

        // 初期化
        surfaceHolder.setFormat(PixelFormat.RGBA_8888)
        surfaceHolder.addCallback(SurfaceHolderCallbackImpl())

        eglManager = EGLManager()
        eglManager.initialize()
    }

    fun onPause() {
        status = Status.PAUSE
    }

    fun onResume() {
        status = Status.RUN
    }

    fun onDestroy() {
        if (status == Status.DESTROY) {
            return
        }
        status = Status.DESTROY
        try {
            // レンダリング停止待ち
            join()
        } catch (e: Exception) {
            // NOP
        }
    }

    private fun isAvailable() = status != Status.DESTROY

    override fun run() {
        // 描画用デバイスが生成されるまで待つ
        while (windowDevice == null) {
            sleep()
        }
        val device = windowDevice!!

        // サーフェイスが使用可能な状態になるまで待つ
        while (!device.isSurfaceAvailable() && isAvailable()) {
            sleep()
        }

        // スレッドにデバイスをバインドする
        device.bind()

        // OpenGL ES の描画処理実行
        app.initialize()

        // メインループ
        while (isAvailable() && !app.isAbort()) {
            if (status == Status.RUN && device.isSurfaceAvailable()) {
                if (!device.isBinded()) {
                    device.bind()
                }

                // サーフェイスサイズが変わっていたら通知する
                if (app.surfaceWidth != device.surfaceWidth ||
                    app.surfaceHeight != device.surfaceHeight) {
                    app.surfaceWidth = device.surfaceWidth
                    app.surfaceHeight = device.surfaceHeight
                    app.resized(device.surfaceWidth, device.surfaceHeight)
                }

                app.rendering(device.surfaceWidth, device.surfaceHeight)
            } else {
                if (device.isBinded()) {
                    device.unbind()
                }
                sleep()
            }
        }

        app.destroy()

        device.unbind()
    }

    fun waitUnbindDevice() {
        if (windowDevice?.isDeviceThread() == true) {
            // 現在のスレッドにバインドされていたら解除する
            windowDevice?.unbind()
        } else {
            // バインドが解除されるまで待つ
            while (windowDevice?.isBinded() == true) {
                sleep()
            }
        }
    }

    /**
     * SurfaceView のライフサイクルに対応したリスナー
     */
    private inner class SurfaceHolderCallbackImpl: SurfaceHolder.Callback {

        // 描画用メモリが確保された時に呼ばれる
        override fun surfaceCreated(holder: SurfaceHolder) {
            if (windowDevice == null) {
                windowDevice = EGLDevice(eglManager)
            }
        }

        // surfaceCreatedの直後とサーフェイスの縦横サイズが変更された直後に呼ばれる
        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            // 一度描画を停止
            val temp = status
            status = Status.PAUSE
            waitUnbindDevice()

            // EGLSurfaceの再構築
            windowDevice?.onSurfaceChanged(holder, width, height)

            // statusを戻す
            status = temp
        }

        // ActivityがonPause状態になったりアプリが終了した場合に呼ばれる
        override fun surfaceDestroyed(holder: SurfaceHolder) {
            if (status != Status.DESTROY) {
                status = Status.PAUSE
            }
            waitUnbindDevice()

            // EGLSurfaceを破棄する
            windowDevice?.onSurfaceDestroyed()

            if (status == Status.DESTROY) {
                // アプリが終了状態であればEGLの解放も行う
                windowDevice?.destroy()
                eglManager.destroy()
            }
        }

    }

    private fun sleep() {
        try {
            sleep(1)
        } catch (e: Exception) { /* NOP */ }
    }
}