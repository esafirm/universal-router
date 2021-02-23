package nolambda.linkrouter.android.measure

import nolambda.linkrouter.android.BaseRoute
import nolambda.linkrouter.android.RouteParam
import nolambda.linkrouter.android.middlewares.MiddleWareResult
import nolambda.linkrouter.android.middlewares.Middleware

class MeasureMiddleWare<Extra>(
    private val measureConfig: MeasureConfig,
    private val middleWares: List<Middleware<Extra>>
) : Middleware<Extra> {
    override fun onRouting(route: BaseRoute<*>, routeParam: RouteParam<*, Extra>): MiddleWareResult<Extra> {
        val initial = MiddleWareResult(route, routeParam)
        return middleWares.fold(initial) { acc, middleware ->
            measureConfig.doMeasure("$middleware") {
                middleware.onRouting(acc.route, acc.routeParam)
            }
        }
    }
}
