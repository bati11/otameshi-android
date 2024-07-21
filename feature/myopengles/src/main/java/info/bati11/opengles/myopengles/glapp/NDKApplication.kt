package info.bati11.opengles.myopengles.glapp

class NDKApplication : GLApplication() {

    private val glAppPtr: Long = 0
    fun postFrontBuffer() {
        windowDevice?.postFrontBuffer()
    }

    external override fun initialize()
    external override fun resized(width: Int, height: Int)
    external override fun rendering(width: Int, height: Int)
    external override fun destroy()
}
