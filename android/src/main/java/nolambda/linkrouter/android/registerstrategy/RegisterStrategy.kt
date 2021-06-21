package nolambda.linkrouter.android.registerstrategy

import nolambda.linkrouter.SimpleRouter
import nolambda.linkrouter.UriRouter
import nolambda.linkrouter.android.BaseRoute
import nolambda.linkrouter.android.RouteHandler
import nolambda.linkrouter.android.UriResult

interface RegisterStrategy<Extra> {
    fun <P : Any, R> register(
        simpleRouter: SimpleRouter<RouteHandler<*, *, *>>,
        uriRouter: UriRouter<UriResult>,
        route: BaseRoute<P>,
        handler: RouteHandler<P, R, Extra>
    )

    fun await()
}
