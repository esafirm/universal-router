package nolambda.linkrouter.android.annotation

import android.content.Context
import android.util.Log
import nolambda.linkrouter.android.BaseRoute
import nolambda.linkrouter.android.Middleware
import nolambda.linkrouter.android.RouterPlugin

typealias NameResolver = (String) -> String

@AutoRegister
class RouteAutoRegisterMiddleware(
    private val plugin: RouterPlugin = RouterPlugin,
    private val nameResolver: NameResolver = { name -> "nolambda.init.route.${name}" }
) : Middleware {

    companion object {
        private const val TAG = "RouteAutoRegister"
    }

    override fun onRouting(route: BaseRoute<*>, param: Any?) {
        if (plugin.isUseAnnotationProcessor.not()) return
        val name = "${route.javaClass.simpleName}RouteInit"
        val fullClassName = nameResolver(name)
        try {
            val routeInit = Class.forName(fullClassName)
            routeInit.getDeclaredConstructor(Context::class.java).newInstance(RouterPlugin.appContext)
        } catch (e: ClassNotFoundException) {
            Log.e(TAG, "No initialization found for $fullClassName")
        }
    }
}