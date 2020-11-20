package nolambda.linkrouter.android.middlewares

import nolambda.linkrouter.android.BaseRoute
import nolambda.linkrouter.android.RouteParam

data class MiddleWareResult<Extra>(
    val route: BaseRoute<*>,
    val routeParam: RouteParam<*, Extra>
)