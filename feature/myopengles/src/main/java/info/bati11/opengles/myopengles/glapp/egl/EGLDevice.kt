package info.bati11.opengles.myopengles.glapp.egl

import android.opengl.GLES20
import android.view.SurfaceHolder
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.egl.EGLSurface

/**
 * EGLSurface と EGLContext のペア
 */
class EGLDevice(private val eglManager: EGLManager) {

    companion object {
        val CONTEXT_ATTRIBUTES = intArrayOf(
            0x3098, // EGL_CONTEXT_CLIENT_VERSIONを表す
            2,
            EGL10.EGL_NONE
        )
    }

    var surfaceWidth = 10
    var surfaceHeight = 14

    private var eglContext: EGLContext? = null
    private var eglSurface: EGLSurface? = null
    private var bindedThread: Thread? = null

    fun isDeviceThread(): Boolean = Thread.currentThread() == bindedThread

    fun isSurfaceAvailable(): Boolean = eglSurface != null && eglSurface != EGL10.EGL_NO_SURFACE

    fun isBinded(): Boolean = bindedThread != null

    /**
     * サーフェス（SurfaceHolder）の生成・サイズ変更が発生
     */
    fun onSurfaceChanged(surfaceHolder: SurfaceHolder, width: Int, height: Int) {
        if (eglContext == null) {
            eglContext = eglManager.egl?.eglCreateContext(
                eglManager.eglDisplay,
                eglManager.eglConfig,
                EGL10.EGL_NO_CONTEXT, // マルチスレッドで使う場合は共有するEGLContextを指定する
                CONTEXT_ATTRIBUTES,
            )
            if (eglContext == EGL10.EGL_NO_CONTEXT) {
                throw IllegalStateException("failed eglCreateContext()")
            }
        }

        this.surfaceWidth = width
        this.surfaceHeight = height

        if (eglSurface != null) {
            eglManager.egl?.eglDestroySurface(eglManager.eglDisplay, eglSurface)
            eglSurface = null
        }

        // EGLSurfaceを生成する
        eglSurface = eglManager.egl?.eglCreateWindowSurface(
            eglManager.eglDisplay,
            eglManager.eglConfig,
            surfaceHolder, // 描画対象となるオブジェクト
            null, // 属性リスト
        )

        // EGLSurfaceの生成に失敗しているかチェックする
        if (eglSurface == EGL10.EGL_NO_SURFACE) {
            throw IllegalStateException("failed eglCreateWindowSurface()")
        }
    }

    /**
     * サーフェスの破棄
     */
    fun onSurfaceDestroyed() {
        if (eglSurface != null) {
            val destroySurface = eglSurface
            eglSurface = null
            eglManager.egl?.eglDestroySurface(eglManager.eglDisplay, destroySurface)
        }
    }

    /**
     * スレッドにバインドする成功すると、他のスレッドからは廃棄やアンバインドできなくなる
     */
    fun bind() {
        val ok = eglManager.egl?.eglMakeCurrent(
            eglManager.eglDisplay,
            eglSurface, // 描画対象のEGLSurface
            eglSurface, // 読み込み対象のEGLSurface
            eglContext,
        )
        if (ok == true) {
            bindedThread = Thread.currentThread()
        }
    }

    fun unbind() {
        GLES20.glFinish()
        val ok = eglManager.egl?.eglMakeCurrent(
            eglManager.eglDisplay,
            EGL10.EGL_NO_SURFACE,
            EGL10.EGL_NO_SURFACE,
            EGL10.EGL_NO_CONTEXT,
        )
        if (ok == true) {
            bindedThread = null
        }
    }

    /**
     * フロントバッファへ反映を行う
     */
    fun postFrontBuffer() {
        if (bindedThread != Thread.currentThread()) {
            return
        }

        val ok = eglManager.egl?.eglSwapBuffers(
            eglManager.eglDisplay, // 描画対象のEGLDisplay
            eglSurface,            // 画面反映を行うEGLSurface
        )
        if (ok != true) {
            throw IllegalStateException("failed eglSwapBuffers")
        }
    }

    fun destroy() {
        if (eglSurface != null) {
            val destroySurface = eglSurface
            eglSurface = null
            val ok = eglManager.egl?.eglDestroySurface(eglManager.eglDisplay, destroySurface)
            if (ok != true) {
                throw IllegalStateException("failed eglDestroySurface")
            }
        }

        if (eglContext != null) {
            val ok = eglManager.egl?.eglDestroyContext(eglManager.eglDisplay, eglContext)
            if (ok != true) {
                throw IllegalStateException("failed eglDestroyContext")
            }
            eglContext = null
        }
    }

}