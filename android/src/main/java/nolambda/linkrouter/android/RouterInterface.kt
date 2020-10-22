package nolambda.linkrouter.android

import nolambda.linkrouter.DeepLinkUri

typealias RouteHandler<P, R, E> = (RouteParam<P, E>) -> R
typealias RouteProcessor<T> = (T, ActionInfo) -> Unit

class RouteParam<Param, Extra>(
    val param: Param? = null,
    val info: ActionInfo,
    var extra: Extra? = null
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
}

interface RouterProcessor<Extra> {
    fun <P : Any, R> register(route: BaseRoute<P>, handler: RouteHandler<P, R, Extra>)
    fun goTo(uri: String): Boolean
    fun <P : Any> push(route: BaseRoute<P>, param: P? = null)
}

interface AppRouter<Extra> : RouterProcessor<Extra>, RouterComponents