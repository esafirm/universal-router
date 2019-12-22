package nolambda.linkrouter.android

import nolambda.linkrouter.SimpleRouter
import nolambda.linkrouter.UriRouter
import nolambda.linkrouter.addEntry

typealias RouteHandler<T> = (RouteResult<T>) -> Unit

class RouteResult<T>(
    val param: T? = null,
    val mapParam: Map<String, String>
)

object Router {
    object AndroidSimpleRouter : SimpleRouter<RouteHandler<*>>()
    object AndroidUriRouter : UriRouter<Unit>()

    private val simpleRouter = AndroidSimpleRouter
    private val uriRouter = AndroidUriRouter

    private val EMPTY_PARAMETER = emptyMap<String, String>()
    private val EMPTY_RESULT = RouteResult<Unit>(mapParam = EMPTY_PARAMETER)

    fun cleanRouter() {
        simpleRouter.clear()
        uriRouter.clear()
    }

    fun <P> register(route: BaseRoute<P>, handler: RouteHandler<P>) {
        val path = route.path

        // Handle non path
        simpleRouter.addEntry(route) {
            @Suppress("UNCHECKED_CAST")
            handler as RouteHandler<*>
        }

        // Handle the path
        if (path.isNotBlank()) {
            uriRouter.addEntry(path) {
                handler.invoke(RouteResult(mapParam = it))
            }
        }
    }

    fun <P> push(route: BaseRoute<P>) {
        simpleRouter.resolve(route).invoke(EMPTY_RESULT)
    }

    fun <P> push(route: RouteWithParam<P>, param: P) {
        simpleRouter.resolve(route).invoke(RouteResult(
            param = param,
            mapParam = EMPTY_PARAMETER
        ))
    }

    fun goTo(uri: String) {
        uriRouter.resolve(uri)
    }
}