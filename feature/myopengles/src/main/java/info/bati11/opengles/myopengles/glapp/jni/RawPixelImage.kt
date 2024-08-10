package info.bati11.opengles.myopengles.glapp.jni

import android.graphics.Bitmap
import java.nio.Buffer
import java.nio.ByteBuffer

data class RawPixelImage(
    /**
     * RGB/RGBA ピクセル情報
     */
    val pixelData: Buffer?,

    /**
     * 画像幅
     */
    val width: Int,

    /**
     * 画像高さ
     */
    val height: Int,

    /**
     * 画像フォーマット
     * GL_RGBA, GL_RGB
     */
    val format: Int,
) {
    companion object {
        fun loadImage(image: Bitmap, pixelFormat: Int): RawPixelImage {
            val imageWidth = image.width
            val imageHeight = image.height
            val pixelBuffer = ByteBuffer.allocateDirect(imageWidth * imageHeight * 4)

            val result = RawPixelImage(pixelBuffer, imageWidth, imageHeight, pixelFormat)

            val temp = IntArray(imageWidth)
            val pixelTemp = ByteArray(4)
            for (i in 0..<imageHeight) {
                // 1行ずつ読む
                image.getPixels(temp, 0, imageWidth, 0, i, imageWidth, 1);
                for (j in 0..<imageWidth) {
                    val pixel = temp[j]
                    pixelTemp[0] = ((pixel shr 16) and 0xFF).toByte()
                    pixelTemp[1] = ((pixel shr 8) and 0xFF).toByte()
                    pixelTemp[2] = (pixel and 0xFF).toByte()
                    pixelTemp[3] = ((pixel shr 24) and 0xFF).toByte()
                    pixelBuffer.put(pixelTemp)
                }
            }

            pixelBuffer.position(0)

            image.recycle()

            return result;
        }
    }

}