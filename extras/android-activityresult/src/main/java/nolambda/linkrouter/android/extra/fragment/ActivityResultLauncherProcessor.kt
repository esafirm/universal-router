package nolambda.linkrouter.android.extra.fragment

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import nolambda.linkrouter.android.BaseRoute
import nolambda.linkrouter.android.RouteWithParam

object ActivityResultLauncherProcessor : RouteWithParam<ActivityResultLauncherProcessor.Param>() {
    data class Param(
        val launcher: ActivityResultLauncher<Intent>,
        val originalParam: Any?,
        val originalRoute: BaseRoute<*>
    )
}