package nolambda.linkrouter.android.extra.caller

import nolambda.linkrouter.android.BaseRoute
import nolambda.linkrouter.android.RouteWithParam

object CallerProcessor : RouteWithParam<CallerProcessor.Param>() {
    data class Param(
        val caller: Any,
        val originalParam: Any? = null,
        val originalRoute: BaseRoute<*>
    )
}
