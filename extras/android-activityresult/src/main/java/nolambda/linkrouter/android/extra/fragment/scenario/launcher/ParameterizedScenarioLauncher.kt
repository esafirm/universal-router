package nolambda.linkrouter.android.extra.fragment.scenario.launcher

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import nolambda.linkrouter.android.RouteWithParam
import nolambda.linkrouter.android.RouterProcessor
import nolambda.linkrouter.android.extra.fragment.pushToProcessor

class ParameterizedScenarioLauncher<P : Any>(
    private val launcher: ActivityResultLauncher<Intent>,
    private val route: RouteWithParam<P>
) {
    fun launch(router: RouterProcessor<*>, p: P) {
        router.pushToProcessor(launcher, route, p)
    }
}