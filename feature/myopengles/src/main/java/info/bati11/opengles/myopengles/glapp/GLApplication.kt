package info.bati11.opengles.myopengles.glapp

abstract class GLApplication {

    var surfaceWidth = 0
    var surfaceHeight = 0

    fun isAbort(): Boolean {
        return false
    }

    abstract fun initialize()
    abstract fun resized(width: Int, height: Int)
    abstract fun rendering(width: Int, height: Int)
    abstract fun destroy()
}