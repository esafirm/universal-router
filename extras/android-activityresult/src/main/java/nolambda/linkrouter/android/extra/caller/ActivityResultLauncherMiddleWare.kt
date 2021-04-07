package nolambda.linkrouter.android.extra.caller

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import nolambda.linkrouter.android.BaseRoute
import nolambda.linkrouter.android.RouteParam
import nolambda.linkrouter.android.middlewares.MiddleWareResult
import nolambda.linkrouter.android.middlewares.Middleware

class ActivityResultLauncherMiddleWare<Extra>(
    private val onCreateExtra: (Extra?, ActivityResultLauncher<Intent>) -> Extra
) : Middleware<Extra> {
    override fun onRouting(route: BaseRoute<*>, routeParam: RouteParam<*, Extra>): MiddleWareResult<Extra> {
        val navParam = routeParam.param
        if (navParam is ActivityResultLauncherProcessor.Param) {
            return MiddleWareResult(navParam.originalRoute, routeParam.copyWithParam(
                passedParam = navParam.originalParam,
                passedExtra = onCreateExtra(routeParam.extra, navParam.launcher)
            ))
        }

        return MiddleWareResult(route, routeParam)
    }
}