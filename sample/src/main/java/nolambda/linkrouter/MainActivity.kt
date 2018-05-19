package nolambda.linkrouter

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val router by lazy { FragmentRouter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handler = Handler(Looper.getMainLooper())

        router.goTo("sample://fragment/first")

        handler.postDelayed({
            router.goTo("sample://fragment/second")
        }, 2000)

        handler.postDelayed({
            router.goTo("sample://fragment/third")
        }, 5000)
    }
}
