package nolambda.linkrouter.android.autoregister

import android.content.Context

@AutoRegister
interface RouteInit {
    fun onInit(appContext: Context)
}