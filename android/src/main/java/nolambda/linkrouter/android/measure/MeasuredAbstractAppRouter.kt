package nolambda.linkrouter.android.measure

import nolambda.linkrouter.android.AbstractAppRouter
import nolambda.linkrouter.android.BaseRoute
import nolambda.linkrouter.android.Route
import nolambda.linkrouter.android.RouteHandler
import nolambda.linkrouter.android.RouteParam
import nolambda.linkrouter.android.RouteWithParam
import nolambda.linkrouter.android.middlewares.Middleware

/**
 * This router is intended for measuring actions that router execute
 */
abstract class MeasuredAbstractAppRouter<Extra>(
    private val config: MeasureConfig,
    vararg middleWares: Middleware<Extra> = emptyArray()
) : AbstractAppRouter<Extra>(*middleWares) {

    override fun createMiddleWares() = measureAndLog("registerAllMiddleWares") {
        super.createMiddleWares()
    }

    override fun cleanRouter() = measureAndLog("cleanRouter") {
        super.cleanRouter()
    }

    override fun <P : Any, R> register(route: BaseRoute<P>, handler: RouteHandler<P, R, Extra>) =
        measureAndLog("register $route") {
            super.register(route, handler)
        }

    override fun <P : Any> push(route: RouteWithParam<P>, param: P) = measureAndLog("push $route") {
        super.push(route, param)
    }

    override fun push(route: Route) = measureAndLog("push $route") {
        super.push(route)
    }

    override fun goTo(uri: String): Boolean = measureAndLog("goTo $uri") {
        super.goTo(uri)
    }

    override fun <P : Any> applyMiddleware(
        route: BaseRoute<*>,
        routeParam: RouteParam<P, Extra>
    ) = measureAndLog("applyMiddleware") {
        super.applyMiddleware(route, routeParam)
    }

    private fun <T> measureAndLog(name: String, block: () -> T): T {
        if (config.shouldMeasure(name).not()) return block()
        return config.doMeasure(name, block)
    }
}