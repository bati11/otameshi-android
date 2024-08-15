package info.bati11.opengles.myopengles.glapp.jni

import android.content.Context
import android.graphics.BitmapFactory
import info.bati11.opengles.myopengles.glapp.GLApplication

class JniApplication(val context: Context) : GLApplication() {

    companion object {
        @JvmStatic
        external fun initializeNative();
    }

    private val glAppPtr: Long = 0
    fun postFrontBuffer() {
        windowDevice?.postFrontBuffer()
    }

    fun loadImage(file_name: String?, pixel_format: Int): RawPixelImage? {
        try {
            context.assets.open(file_name!!).use { stream ->
                val image = BitmapFactory.decodeStream(stream)
                return RawPixelImage.loadImage(image, pixel_format!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    external override fun initialize()
    external override fun resized(width: Int, height: Int)
    external override fun rendering(width: Int, height: Int)
    external override fun destroy()
}
