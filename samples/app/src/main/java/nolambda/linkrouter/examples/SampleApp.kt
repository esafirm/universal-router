package nolambda.linkrouter.examples

import android.app.Application
import android.util.Log
import nolambda.linkrouter.android.RouterPlugin
import nolambda.linkrouter.approuter.AppRouter
import nolambda.linkrouter.approuter.AppRoutes

class SampleApp : Application() {
    override fun onCreate() {
        super.onCreate()

        RouterPlugin.logger = { log: String -> Log.d("Router", log) }
    }
}