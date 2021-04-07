package nolambda.linkrouter.android.extra.caller

import nolambda.linkrouter.android.BaseRoute
import nolambda.linkrouter.android.RouteParam
import nolambda.linkrouter.android.middlewares.MiddleWareResult
import nolambda.linkrouter.android.middlewares.Middleware

class CallerProviderMiddleware<Extra>(
    private val onCreateExtra: (Extra?, Any?) -> Extra
) : Middleware<Extra> {
    override fun onRouting(route: BaseRoute<*>, routeParam: RouteParam<*, Extra>): MiddleWareResult<Extra> {
        val navParam = routeParam.param
        if (route is CallerProcessor && navParam is CallerProcessor.Param) {
            return MiddleWareResult(navParam.originalRoute, routeParam.copyWithParam(
                passedParam = navParam.originalParam,
                passedExtra = onCreateExtra(routeParam.extra, navParam.caller)
            ))
        }

        return MiddleWareResult(route, routeParam)
    }
}