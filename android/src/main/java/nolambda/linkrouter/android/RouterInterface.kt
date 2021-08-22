package nolambda.linkrouter.android

typealias RouteHandler<P, R, E> = (RouteParam<P, E>) -> R

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

interface RouterComponents {
    fun cleanRouter()
    fun removeProcessor(processor: RouteProcessor<*>)
    fun addProcessor(canHandle: (result: Any?) -> Boolean, processor: RouteProcessor<*>)
}

interface RouterProcessor<Extra> {
    fun <P : Any, R> register(route: BaseRoute<P>, handler: RouteHandler<P, R, Extra>)

    /**
     * @param uri - URI to be resolved
     * @return null if there's no matching uri register in the router
     * otherwise return [UriResult]
     */
    fun resolveUri(uri: String): UriResult?

    /**
     * @param uri - URI to be resolved
     * @return false if there's no matching uri
     */
    fun canHandle(uri: String): Boolean


    fun goTo(uri: String): RouteResult
    fun push(route: Route): RouteResult
    fun <P : Any> push(route: RouteWithParam<P>, param: P): RouteResult
}

interface AppRouter<Extra> : RouterProcessor<Extra>, RouterComponents

interface StackRouter {
    fun replace(route: Route)
    fun pop()
    fun popUntil(routeFinder: (Route) -> Boolean)
}
