package nolambda.linkrouter.examples.notsimple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import nolambda.linkrouter.android.Router
import nolambda.linkrouter.approuter.AppRoutes
import nolambda.linkrouter.examples.R

class NotSimpleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        AppRoutes.Home.register {
//            HomeScreen()
//        }

//        Router.push(AppRoutes.Home)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, HomeScreen())
            .commit()
    }

}