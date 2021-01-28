package nolambda.linkrouter.android.extra.fragment

import androidx.fragment.app.Fragment
import nolambda.linkrouter.android.BaseRoute
import nolambda.linkrouter.android.RouteParam
import nolambda.linkrouter.android.middlewares.MiddleWareResult
import nolambda.linkrouter.android.middlewares.Middleware

class FragmentProviderMiddleware<Extra>(
    private val onCreateExtra: (Extra?, Fragment) -> Extra
) : Middleware<Extra> {
    override fun onRouting(route: BaseRoute<*>, routeParam: RouteParam<*, Extra>): MiddleWareResult<Extra> {
        val navParam = routeParam.param
        if (navParam is FragmentProcessor.Param) {
            return MiddleWareResult(navParam.originalRoute, routeParam.copyWithParam(
                passedParam = navParam.originalParam,
                passedExtra = onCreateExtra(routeParam.extra, navParam.fragment)
            ))
        }

        return MiddleWareResult(route, routeParam)
    }
}