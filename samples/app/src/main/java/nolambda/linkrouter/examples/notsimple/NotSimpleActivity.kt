package nolambda.linkrouter.examples.notsimple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import nolambda.linkrouter.android.Router
import nolambda.linkrouter.android.RouterPlugin
import nolambda.linkrouter.approuter.AppRoutes
import nolambda.linkrouter.examples.R

class NotSimpleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RouterPlugin.appContext = this.applicationContext
        RouterPlugin.isUseAnnotationProcessor = true

        AppRoutes.Home.register {
            HomeScreen()
        }

        val manager = supportFragmentManager
        Router.addProcessorWithLifecycle<Fragment>(this) {
            manager.beginTransaction()
                .replace(R.id.container, it)
                .commit()
        }

        Router.push(AppRoutes.Home)
    }

}