@file:Suppress("UNCHECKED_CAST")

package nolambda.linkrouter.android

import nolambda.linkrouter.SimpleRouter
import nolambda.linkrouter.UriRouter
import nolambda.linkrouter.android.middlewares.MiddleWareResult
import nolambda.linkrouter.android.middlewares.Middleware
import nolambda.linkrouter.android.registerstrategy.EagerRegisterStrategy
import nolambda.linkrouter.android.registerstrategy.RegisterStrategy
import kotlin.system.measureTimeMillis

abstract class AbstractAppRouter<Extra>(
    vararg middleWares: Middleware<Extra> = emptyArray(),
    private val registerStrategy: RegisterStrategy<Extra> = EagerRegisterStrategy()
) : AppRouter<Extra> {

    private class AndroidSimpleRouter : SimpleRouter<RouteHandler<*, *, *>>()
    private class AndroidUriRouter : UriRouter<UriResult>(RouterPlugin.logger)

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
        registerStrategy.register(simpleRouter, uriRouter, route, handler)
    }

    override fun resolveUri(uri: String): UriResult? {
        val time = measureTimeMillis { registerStrategy.await() }
        println("Time is $time")
        return uriRouter.resolve(uri)
    }

    override fun canHandle(uri: String): Boolean {
        return resolveUri(uri) != null
    }

    override fun goTo(uri: String): RouteResult {
        return try {
            val result = resolveUri(uri) ?: return RouteResult(false)
            val (deepLinkUri, route, param) = result
            val routeParam = if (route is RouteWithParam<*>) {
                route.mapUri(deepLinkUri, param)
            } else null
            val routeResult = processRoute(route as BaseRoute<Any>, routeParam, createInfo(uri))
            RouteResult(true, routeResult)
        } catch (e: Exception) {
            e.handleError()
            RouteResult(false)
        }
    }

    override fun <P : Any> push(route: RouteWithParam<P>, param: P): RouteResult {
        return try {
            val result = processRoute(route, param, createInfo())
            RouteResult(true, result)
        } catch (e: Exception) {
            e.handleError()
            RouteResult(false)
        }
    }

    override fun push(route: Route): RouteResult {
        return try {
            val result = processRoute(route, null, createInfo())
            RouteResult(true, result)
        } catch (e: Exception) {
            e.handleError()
            RouteResult(false)
        }
    }

    private fun Throwable.handleError() = RouterPlugin.errorHandler(this)

    private fun <P : Any> processRoute(
        route: BaseRoute<P>,
        param: P?,
        actionInfo: ActionInfo
    ): Any? {
        val routeParam = RouteParam<P, Extra>(
            info = actionInfo,
            param = param
        )
        val (finalRoute, finalParam) = applyMiddleware(route, routeParam)
        val result = simpleResolve(finalRoute, finalParam)
        invokeProcessor(result, actionInfo)
        return result
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