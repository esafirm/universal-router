package nolambda.linkrouter.android

import nolambda.linkrouter.DeepLinkUri

typealias RouteHandler<P, R, E> = (RouteParam<P, E>) -> R
typealias RouteProcessor<T> = (T, ActionInfo) -> Unit

class RouteParam<Param, Extra>(
    val param: Param? = null,
    val info: ActionInfo,
    var extra: Extra? = null
) {
    fun <P> copyWithParam(
        passedParam: P?,
        passedInfo: ActionInfo = info,
        passedExtra: Extra? = extra
    ) = RouteParam(passedParam, passedInfo, passedExtra)
}

/**
 * Contain info regarding the action that trigger the routing
 */
data class ActionInfo(
    val currentRouter: AbstractAppRouter<*>,
    val uri: String? = null
) {
    val isTriggeredByUri = uri.isNullOrEmpty().not()
}

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
    fun canHandle(uri: String): Boolean
    fun push(route: Route)
    fun <P : Any> push(route: RouteWithParam<P>, param: P)
}

interface AppRouter<Extra> : RouterProcessor<Extra>, RouterComponents