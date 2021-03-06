package nolambda.linkrouter.examples.notsimple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import nolambda.linkrouter.android.RouterPlugin
import nolambda.linkrouter.android.addRouterProcessor
import nolambda.linkrouter.android.autoregister.AutoRegister
import nolambda.linkrouter.approuter.AppRouter
import nolambda.linkrouter.approuter.AppRoutes
import nolambda.linkrouter.approuter.register
import nolambda.linkrouter.examples.R

class NotSimpleActivity : AppCompatActivity() {

    @AutoRegister
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RouterPlugin.run {
            appContext = this@NotSimpleActivity.applicationContext
            isUseAnnotationProcessor = true
        }

        AppRoutes.Home.register {
            HomeScreen()
        }

        addRouterProcessor<Fragment>(AppRouter) { fragment, _ ->
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
        }

        AppRouter.push(AppRoutes.Home)
    }
}