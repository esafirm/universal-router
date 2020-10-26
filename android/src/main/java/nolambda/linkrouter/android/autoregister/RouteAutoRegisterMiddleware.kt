package nolambda.linkrouter.android.autoregister

import android.content.Context
import android.util.Log
import nolambda.linkrouter.android.BaseRoute
import nolambda.linkrouter.android.Middleware
import nolambda.linkrouter.android.RouteParam
import nolambda.linkrouter.android.RouterPlugin

typealias NameResolver = (String) -> String

@AutoRegister
class RouteAutoRegisterMiddleware(
    private val plugin: RouterPlugin = RouterPlugin,
    private val nameResolver: NameResolver = { name -> "nolambda.init.route.${name}" }
) : Middleware<Any> {

    companion object {
        private const val TAG = "RouteAutoRegister"
    }

    override fun onRouting(route: BaseRoute<*>, routeParam: RouteParam<*, Any>): BaseRoute<*> {
        if (plugin.isUseAnnotationProcessor.not()) return route
        val name = "${route.javaClass.simpleName}RouteInit"
        val fullClassName = nameResolver(name)
        try {
            val routeInit = Class.forName(fullClassName)
            routeInit.getDeclaredConstructor(Context::class.java).newInstance(plugin.appContext)
        } catch (e: ClassNotFoundException) {
            Log.e(TAG, "No initialization found for $fullClassName")
        }
        return route
    }
}