package info.bati11.opengles.myopengles.glapp

class NDKApplication : GLApplication() {

    companion object {
        var currentRenderThread: OpenGlEsRenderThread? = null

        @JvmStatic
        external fun initializeNative()

        @JvmStatic
        fun postFrontBuffer() {
            currentRenderThread?.windowDevice?.postFrontBuffer()
        }
    }

    external override fun initialize()
    external override fun resized(width: Int, height: Int)
    external override fun rendering(width: Int, height: Int)
    external override fun destroy()
}
