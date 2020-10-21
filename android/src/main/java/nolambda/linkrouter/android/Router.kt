@file:Suppress("UNCHECKED_CAST")

package nolambda.linkrouter.android

import nolambda.linkrouter.SimpleRouter
import nolambda.linkrouter.UriRouter
import nolambda.linkrouter.addEntry

object Router : RouterProcessor, RouterComponents {

    private class AndroidSimpleRouter : SimpleRouter<RouteHandler<*, *>>()
    private class AndroidUriRouter : UriRouter<UriRoute>(RouterPlugin.logger)

    private val simpleRouter by lazy { AndroidSimpleRouter() }
    private val uriRouter by lazy { AndroidUriRouter() }

    private val middlewares = linkedSetOf<Middleware>()
    private val processors = linkedSetOf<Pair<Class<*>, RouteProcessor<in Any>>>()

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

    override fun addMiddleware(middleware: Middleware) {
        middlewares.add(middleware)
    }

    override fun removeMiddleware(middleware: Middleware) {
        middlewares.remove(middleware)
    }

    /* --------------------------------------------------- */
    /* > Processing */
    /* --------------------------------------------------- */

    override fun <P : Any, R> register(route: BaseRoute<P>, handler: RouteHandler<P, R>) {
        val paths = route.routePaths

        // Handle non path
        simpleRouter.addEntry(route) {
            @Suppress("UNCHECKED_CAST")
            handler as RouteHandler<*, *>
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

    override fun goTo(uri: String): Boolean {
        try {
            val result = uriRouter.resolve(uri) ?: return false
            val (deepLinkUri, route, param) = result
            val routeParam = if (route is RouteWithParam<*>) {
                route.mapUri(deepLinkUri, param)
            } else null
            processRoute(route, routeParam, ActionInfo(true))
            return true
        } catch (e: Exception) {
            e.handleError()
        }
        return false
    }

    override fun <P : Any> push(route: BaseRoute<P>, param: P?) {
        try {
            if (route is RouteWithParam<*> && param == null) {
                throw IllegalArgumentException("param must be not null for $route")
            }

            processRoute(route, param, ActionInfo(false))
        } catch (e: Exception) {
            e.handleError()
        }
    }

    private fun Throwable.handleError() = RouterPlugin.errorHandler(this)

    private fun <P : Any> processRoute(route: BaseRoute<P>, param: P?, actionInfo: ActionInfo) {
        applyMiddleware(route, param)
        val routeParam = RouteParam(
            info = actionInfo,
            param = param
        )
        invokeProcessor(simpleResolve(route, routeParam), actionInfo)
    }

    private fun applyMiddleware(route: BaseRoute<*>, param: Any?) {
        middlewares.forEach {
            it.onRouting(route, param)
        }
    }

    private fun simpleResolve(
        route: BaseRoute<*>,
        param: RouteParam<*>
    ) = simpleRouter.resolve(route).invoke(param)

    private fun invokeProcessor(result: Any?, info: ActionInfo) {
        if (result == null) return
        if (processors.isEmpty()) return

        val clazz = result.javaClass
        processors.forEach { (c, processor) ->
            if (c.isAssignableFrom(clazz)) {
                processor.invoke(result, info)
            }
        }
    }
}