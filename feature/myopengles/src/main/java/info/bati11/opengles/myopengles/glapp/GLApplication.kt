package info.bati11.opengles.myopengles.glapp

import info.bati11.opengles.myopengles.glapp.egl.EGLDevice
import info.bati11.opengles.myopengles.glapp.egl.EGLManager

abstract class GLApplication {

    var surfaceWidth = 0
    var surfaceHeight = 0

    lateinit var eglManager: EGLManager
    var windowDevice: EGLDevice? = null

    fun isAbort(): Boolean {
        return false
    }

    abstract fun initialize()
    abstract fun resized(width: Int, height: Int)
    abstract fun rendering(width: Int, height: Int)
    abstract fun destroy()
}