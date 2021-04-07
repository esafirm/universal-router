package nolambda.linkrouter.android.extra.caller.scenario.launcher

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import nolambda.linkrouter.android.Route
import nolambda.linkrouter.android.RouterProcessor
import nolambda.linkrouter.android.extra.caller.pushToProcessor

class ScenarioLauncher(
    private val launcher: ActivityResultLauncher<Intent>,
    private val route: Route
) {
    fun launch(router: RouterProcessor<*>) {
        router.pushToProcessor(launcher, route)
    }
}
