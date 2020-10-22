package nolambda.linkrouter.approuter

import nolambda.linkrouter.DeepLinkUri
import nolambda.linkrouter.android.AbstractAppRouter
import nolambda.linkrouter.android.BaseRoute
import nolambda.linkrouter.android.Middleware
import nolambda.linkrouter.android.Route
import nolambda.linkrouter.android.RouteHandler
import nolambda.linkrouter.android.RouteParam
import nolambda.linkrouter.android.RouteWithParam
import nolambda.linkrouter.android.autoregister.AutoRegister
import nolambda.linkrouter.android.autoregister.RouteAutoRegisterMiddleware
import nolambda.linkrouter.optString

class AppRoutes {
    object Home : Route()
    object Cart : Route(*AppPath.paths("/cart"))
    object Product : RouteWithParam<Product.ProductParam>(
        *AppPath.paths("/product/{id}")
    ) {
        data class ProductParam(
            val productId: String
        )

        override fun mapUri(uri: DeepLinkUri, raw: Map<String, String>): ProductParam {
            return ProductParam(raw.optString("id", ""))
        }
    }
}

data class AppState(
    val isLoggedIn: Boolean,
    val heavyState: () -> String
)

private val logMiddleWare = object : Middleware<AppState> {
    override fun onRouting(route: BaseRoute<*>, routeParam: RouteParam<*, AppState>) {
        routeParam.extra = AppState(
            isLoggedIn = false,
            heavyState = {
                Thread.sleep(1000)
                "This is a message"
            }
        )
    }
}

@OptIn(AutoRegister::class)
object AppRouter : AbstractAppRouter<AppState>(
    logMiddleWare,
    RouteAutoRegisterMiddleware() as Middleware<AppState>
)

fun <P : Any, R> BaseRoute<P>.register(handler: RouteHandler<P, R, AppState>) {
    AppRouter.register(this, handler)
}