package info.bati11.opengles.myopengles.glapp

import android.os.Bundle
import android.view.SurfaceView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import info.bati11.opengles.myopengles.glapp.jni.JniApplication

class MyOpenGlEsActivity : AppCompatActivity() {

    companion object {
        init {
            System.loadLibrary("glapp");
            JniApplication.initializeNative();
        }
    }

    private lateinit var renderThread: OpenGlEsRenderThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle("My OpenGL ES")
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        setContentView(layout)
        val textView = TextView(this)
        textView.setText("textだよ")
        layout.addView(textView)

        // JNIするためのオブジェクトを用意
        val glApp = JniApplication()

        // OpenGL ES 描画用のスレッドを用意
        renderThread = OpenGlEsRenderThread(glApp)

        // SurfaceViewを用意 ( TODO: TextureViewを選べるようにする
        val surfaceView = SurfaceView(this)
        setContentView(surfaceView)

        renderThread.initialize(surfaceView)
        renderThread.start()
    }

    override fun onPause() {
        super.onPause()
        renderThread.onPause()

        // onDestroyが呼ばれない場合を考慮
        if (isFinishing) {
            renderThread.onDestroy()
        }
    }

    override fun onResume() {
        super.onResume()
        renderThread.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        renderThread.onDestroy()
    }
}