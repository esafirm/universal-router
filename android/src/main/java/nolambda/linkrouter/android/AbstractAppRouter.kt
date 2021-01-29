@file:Suppress("UNCHECKED_CAST")

package nolambda.linkrouter.android

import nolambda.linkrouter.SimpleRouter
import nolambda.linkrouter.UriRouter
import nolambda.linkrouter.addEntry
import nolambda.linkrouter.android.middlewares.MiddleWareResult
import nolambda.linkrouter.android.middlewares.Middleware

abstract class AbstractAppRouter<Extra>(
    vararg middleWares: Middleware<Extra> = emptyArray()
) : AppRouter<Extra> {

    private class AndroidSimpleRouter : SimpleRouter<RouteHandler<*, *, *>>()
    private class AndroidUriRouter : UriRouter<UriRoute>(RouterPlugin.logger)

    private val simpleRouter by lazy { AndroidSimpleRouter() }
    private val uriRouter by lazy { AndroidUriRouter() }

    private val middlewares by lazy { createMiddleWares() }
    private val processors = linkedSetOf<Pair<Class<*>, RouteProcessor<in Any>>>()

    private val _middleWares = middleWares

    protected open fun createMiddleWares(): Set<Middleware<Extra>> {
        val set = linkedSetOf<Middleware<Extra>>()
        set.addAll(_middleWares)
        return set
    }

    /* --------------------------------------------------- */
    /* > Component setup */
    /* --------------------------------------------------- */

    override fun cleanRouter() {
        simpleRouter.clear()
        uriRouter.clear()
        processors.clear()
    }

    override fun removeProcessor(processor: RouteProcessor<*>) {
        processors.removeAll { it.second == processor }
    }

    override fun <T> addProcessor(clazz: Class<T>, processor: RouteProcessor<T>) {
        processors.add(clazz to processor as RouteProcessor<in Any>)
    }

    inline fun <reified T> addProcessor(noinline processor: RouteProcessor<T>) {
        addProcessor(T::class.java, processor)
    }

    /* --------------------------------------------------- */
    /* > Processing */
    /* --------------------------------------------------- */

    override fun <P : Any, R> register(route: BaseRoute<P>, handler: RouteHandler<P, R, Extra>) {
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
            uriRouter.addEntry(path) { uri, param ->
                UriRoute(uri, route as BaseRoute<Any>, param)
            }
        }
    }

    private fun resolveUri(uri: String) = uriRouter.resolve(uri)

    override fun canHandle(uri: String): Boolean {
        return resolveUri(uri) != null
    }

    override fun goTo(uri: String): Boolean {
        try {
            val result = resolveUri(uri) ?: return false
            val (deepLinkUri, route, param) = result
            val routeParam = if (route is RouteWithParam<*>) {
                route.mapUri(deepLinkUri, param)
            } else null
            processRoute(route, routeParam, createInfo(uri))
            return true
        } catch (e: Exception) {
            e.handleError()
        }
        return false
    }

    override fun <P : Any> push(route: RouteWithParam<P>, param: P) {
        try {
            processRoute(route, param, createInfo())
        } catch (e: Exception) {
            e.handleError()
        }
    }

    override fun push(route: Route) {
        try {
            processRoute(route, null, createInfo())
        } catch (e: Exception) {
            e.handleError()
        }
    }

    private fun Throwable.handleError() = RouterPlugin.errorHandler(this)

    private fun <P : Any> processRoute(route: BaseRoute<P>, param: P?, actionInfo: ActionInfo) {
        val routeParam = RouteParam<P, Extra>(
            info = actionInfo,
            param = param
        )
        val (finalRoute, finalParam) = applyMiddleware(route, routeParam)
        invokeProcessor(simpleResolve(finalRoute, finalParam), actionInfo)
    }

    protected open fun <P : Any> applyMiddleware(
        route: BaseRoute<*>,
        routeParam: RouteParam<P, Extra>
    ): MiddleWareResult<Extra> {
        val initial = MiddleWareResult(route, routeParam)
        return middlewares.fold(initial) { acc, middleware ->
            middleware.onRouting(acc.route, acc.routeParam)
        }
    }

    private fun simpleResolve(
        route: BaseRoute<*>,
        param: RouteParam<*, *>
    ) = simpleRouter.resolve(route).invoke(param)

    protected open fun invokeProcessor(result: Any?, info: ActionInfo) {
        if (result == null) return
        if (processors.isEmpty()) return

        val clazz = result.javaClass
        processors.forEach { (c, processor) ->
            if (c.isAssignableFrom(clazz)) {
                processor.invoke(result, info)
            }
        }
    }

    private fun createInfo(url: String? = null): ActionInfo = ActionInfo(this, url)
}