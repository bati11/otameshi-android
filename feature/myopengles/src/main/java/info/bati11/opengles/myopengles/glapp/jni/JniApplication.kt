package info.bati11.opengles.myopengles.glapp.jni

import info.bati11.opengles.myopengles.glapp.GLApplication

class JniApplication : GLApplication() {

    companion object {
        @JvmStatic
        external fun initializeNative();
    }

    private val glAppPtr: Long = 0
    fun postFrontBuffer() {
        windowDevice?.postFrontBuffer()
    }

    external override fun initialize()
    external override fun resized(width: Int, height: Int)
    external override fun rendering(width: Int, height: Int)
    external override fun destroy()
}
