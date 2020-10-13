package nolambda.linkrouter.android.annotation

import android.content.Context

@AutoRegister
interface RouteInit {
    fun onInit(appContext: Context)
}