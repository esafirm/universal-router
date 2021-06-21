package nolambda.linkrouter.android.registerstrategy

import nolambda.linkrouter.SimpleRouter
import nolambda.linkrouter.UriRouter
import nolambda.linkrouter.addEntry
import nolambda.linkrouter.android.BaseRoute
import nolambda.linkrouter.android.RouteHandler
import nolambda.linkrouter.android.UriResult

class EagerRegisterStrategy<Extra> : RegisterStrategy<Extra> {

    override fun <P : Any, R> register(
        simpleRouter: SimpleRouter<RouteHandler<*, *, *>>,
        uriRouter: UriRouter<UriResult>,
        route: BaseRoute<P>,
        handler: RouteHandler<P, R, Extra>
    ) {
        val paths = route.routePaths

        // Handle non path
        simpleRouter.addEntry(route) {
            @Suppress("UNCHECKED_CAST")
            handler as RouteHandler<*, *, *>
        }

        // Handle the path
        if (paths.isEmpty()) return

        paths.forEach { path ->
            if (path.isBlank()) return@forEach
            uriRouter.addEntry(path, matcher = route.pathMatcher()) { uri, param ->
                UriResult(uri, route, param)
            }
        }
    }

    override fun await() {
        // No operation needed, because all operations is finished on register
    }
}