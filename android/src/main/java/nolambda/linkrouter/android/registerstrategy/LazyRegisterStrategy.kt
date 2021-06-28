package nolambda.linkrouter.android.registerstrategy

import nolambda.linkrouter.SimpleRouter
import nolambda.linkrouter.UriRouter
import nolambda.linkrouter.android.BaseRoute
import nolambda.linkrouter.android.RouteHandler
import nolambda.linkrouter.android.UriResult
import java.util.concurrent.Executors
import java.util.concurrent.Future

class LazyRegisterStrategy<Extra> : RegisterStrategy<Extra> {

    private val futures = mutableSetOf<Future<*>>()
    private val executor = Executors.newFixedThreadPool(2)

    override fun <P : Any, R> register(
        simpleRouter: SimpleRouter<RouteHandler<*, *, *>>,
        uriRouter: UriRouter<UriResult>,
        route: BaseRoute<P>,
        handler: RouteHandler<P, R, Extra>
    ) {
        // Handle non path
        simpleRouter.addEntry(route) {
            @Suppress("UNCHECKED_CAST")
            handler as RouteHandler<*, *, *>
        }

        // Handle the path
        val paths = route.routePaths
        if (paths.isEmpty()) return

        val future = executor.submit {
            paths.forEach { path ->
                if (path.isBlank()) return@forEach
                uriRouter.addEntry(path, matcher = route.pathMatcher()) { uri, param ->
                    UriResult(uri, route, param)
                }
            }
        }
        futures.add(future)
    }

    override fun await() {
        val clone = futures.toList()
        futures.clear()
        clone.forEach {
            it.get()
        }
    }
}