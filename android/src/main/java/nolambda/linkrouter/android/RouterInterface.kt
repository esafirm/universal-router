package nolambda.linkrouter.android

import nolambda.linkrouter.DeepLinkUri

typealias RouteHandler<P, R> = (RouteParam<P>) -> R
typealias RouteProcessor<T> = (T, ActionInfo) -> Unit

class RouteParam<T>(
    val param: T? = null,
    val info: ActionInfo
)

/**
 * Contain info regarding the action that trigger the routing
 */
data class ActionInfo(
    val isTriggeredByUri: Boolean
)

/**
 * URI Router resolved data
 */
internal data class UriRoute(
    val uri: DeepLinkUri,
    val route: BaseRoute<Any>,
    val param: Map<String, String>
)

interface RouterComponents {
    fun cleanRouter()
    fun removeProcessor(processor: RouteProcessor<*>)
    fun <T> addProcessor(clazz: Class<T>, processor: RouteProcessor<T>)
    fun addMiddleware(middleware: Middleware)
    fun removeMiddleware(middleware: Middleware)
}

interface RouterProcessor {
    fun <P : Any, R> register(route: BaseRoute<P>, handler: RouteHandler<P, R>)
    fun goTo(uri: String): Boolean
    fun <P : Any> push(route: BaseRoute<P>, param: P? = null)
}