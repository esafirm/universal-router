package nolambda.linkrouter.android.autoregister

import android.content.Context
import nolambda.linkrouter.android.BaseRoute
import nolambda.linkrouter.android.RouteParam
import nolambda.linkrouter.android.RouterPlugin
import nolambda.linkrouter.android.middlewares.MiddleWareResult
import nolambda.linkrouter.android.middlewares.Middleware

typealias NameResolver = (String) -> String

@AutoRegister
class RouteAutoRegisterMiddleware(
    private val plugin: RouterPlugin = RouterPlugin,
    private val nameResolver: NameResolver = { name -> "nolambda.init.route.${name}" }
) : Middleware<Any> {

    companion object {
        private const val TAG = "RouteAutoRegister"
    }

    override fun onRouting(route: BaseRoute<*>, routeParam: RouteParam<*, Any>): MiddleWareResult<Any> {
        if (plugin.isUseAnnotationProcessor.not()) return MiddleWareResult(route, routeParam)
        val name = "${route.javaClass.simpleName}RouteInit"
        val fullClassName = nameResolver(name)
        try {
            val appContext = plugin.appContext
            val routeInit = Class.forName(fullClassName)
            val instance = routeInit.getDeclaredConstructor(Context::class.java).newInstance(appContext)

            instance as RouteInit
            instance.onInit(appContext)

        } catch (e: ClassNotFoundException) {
            plugin.logger?.invoke("No initialization found for $fullClassName")
        }
        return MiddleWareResult(route, routeParam)
    }
}