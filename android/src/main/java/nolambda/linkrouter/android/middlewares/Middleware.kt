package nolambda.linkrouter.android.middlewares

import nolambda.linkrouter.android.BaseRoute
import nolambda.linkrouter.android.RouteParam

interface Middleware<Extra> {
    fun onRouting(route: BaseRoute<*>, routeParam: RouteParam<*, Extra>): MiddleWareResult<Extra>
}