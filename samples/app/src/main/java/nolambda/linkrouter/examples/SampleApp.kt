package nolambda.linkrouter.examples

import android.app.Application
import android.util.Log
import nolambda.linkrouter.android.RouterPlugin

class SampleApp : Application() {
    override fun onCreate() {
        super.onCreate()

        RouterPlugin.logger = { log: String -> Log.d("Router", log) }
    }
}