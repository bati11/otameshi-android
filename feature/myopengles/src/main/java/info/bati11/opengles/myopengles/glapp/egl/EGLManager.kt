package info.bati11.opengles.myopengles.glapp.egl

import android.util.Log
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.egl.EGLDisplay

class EGLManager {

    var egl: EGL10? = null

    /**
     * 描画先ディスプレイ
     * 複数のEGLContextやEGLSurfaceを管理できる
     */
    var eglDisplay: EGLDisplay? = null

    var eglConfig: EGLConfig? = null

    fun initialize() {
        if (egl != null) throw IllegalStateException("already initialized")

        egl = EGLContext.getEGL() as EGL10
        eglDisplay = egl!!.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY)

        val versions = IntArray(2)
        val ok = egl!!.eglInitialize(
            eglDisplay, // 初期化対象のディスプレイ
            versions,   // 初期化されたEGLバージョンを格納する配列
        )
        if (!ok) {
            throw RuntimeException("egl.eglInitialize() is failed.")
        }

        // 実際のバージョンとは異なり"1.0"で固定
        Log.i("EGLManager", "initialize major:${versions[0]}, minor:${versions[1]}")

        eglConfig = chooseConfig(16, 8)
    }

    /**
     * @param depthBits サーフェスの深度バッファが持つビット深度
     * @param stencilBits サーフェスのステンシルバッファが持つビット深度
     */
    private fun chooseConfig(depthBits: Int, stencilBits: Int): EGLConfig {
        val configs = arrayOfNulls<EGLConfig?>(32)
        val configNums = IntArray(1)
        val attrList = getRequireConfigSpec(depthBits, stencilBits)
        Log.i("EGLConfig", "chooseConfig. eglDisplay:$eglDisplay, var2:$attrList, configs:$configs, configs.size:${configs.size}, configNumbs:$configNums")
        val ok = egl?.eglChooseConfig(
            eglDisplay,
            attrList, // 要求するEGLConfigの内容
            configs,  // 結果が保存される配列
            configs.size,
            configNums)
        if (ok != true) {
            throw RuntimeException("failed eglChooseConfig()")
        }
        val configNum = configNums[0]
        val rBits = 8
        val gBits = 8
        val bBits = 8
        val aBits = 8
        for (i in 0..< configNum) {
            val checkConfig = configs[i]
            if (checkConfig != null) {
                val configR = getConfigAttr(checkConfig, EGL10.EGL_RED_SIZE)
                val configG = getConfigAttr(checkConfig, EGL10.EGL_GREEN_SIZE)
                val configB = getConfigAttr(checkConfig, EGL10.EGL_BLUE_SIZE)
                val configA = getConfigAttr(checkConfig, EGL10.EGL_ALPHA_SIZE)
                val configD = getConfigAttr(checkConfig, EGL10.EGL_DEPTH_SIZE)
                val configS = getConfigAttr(checkConfig, EGL10.EGL_STENCIL_SIZE)

                // RGBが指定サイズジャスト、ADSが指定サイズ以上あれば合格とする
                if (configR == rBits && configG == gBits && configB == bBits
                    && configA >= aBits && configD >= depthBits && configS >= stencilBits) {
                    Log.i("EGLConfig", "R($configR) G($configG) B($configB) A($configA) D($configD) S($configS)")
                    return checkConfig
                }
            }
        }
        return configs[0]!!
    }

    private fun getRequireConfigSpec(depthBits: Int, stencilBits: Int): IntArray {
        val result = mutableListOf<Int>()

        // レンダラー OpenGL ES2.0
        result.add(EGL10.EGL_RENDERABLE_TYPE)
        result.add(4) // OpenGL ES 2.0を指定する値

        // カラーバッファ RGBA8
        result.add(EGL10.EGL_RED_SIZE)
        result.add(8)
        result.add(EGL10.EGL_GREEN_SIZE)
        result.add(8)
        result.add(EGL10.EGL_BLUE_SIZE)
        result.add(8)
        result.add(EGL10.EGL_ALPHA_SIZE)
        result.add(8)

        // 深度バッファ
        result.add(EGL10.EGL_DEPTH_SIZE)
        result.add(depthBits)

        // ステンシルバッファ
        result.add(EGL10.EGL_STENCIL_SIZE)
        result.add(stencilBits)

        result.add(EGL10.EGL_NONE)
        val resultArray = IntArray(result.size)
        for (i in 0..< result.size) {
            resultArray[i] = result[i]
        }
        return resultArray
    }

    private fun getConfigAttr(eglConfig: EGLConfig, attrId: Int): Int {
        val value = IntArray(1)
        egl?.eglGetConfigAttrib(eglDisplay, eglConfig, attrId, value)
        return value[0]
    }

    fun destroy() {
        if (egl == null) {
            return
        } else {
            egl?.eglTerminate(eglDisplay)
            eglDisplay = null
            eglConfig = null
            egl = null
        }
    }
}