package nolambda.linkrouter.approuter

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import nolambda.linkrouter.DeepLinkUri
import nolambda.linkrouter.android.BaseRoute
import nolambda.linkrouter.android.Route
import nolambda.linkrouter.android.RouteHandler
import nolambda.linkrouter.android.RouteParam
import nolambda.linkrouter.android.RouteWithParam
import nolambda.linkrouter.android.autoregister.AutoRegister
import nolambda.linkrouter.android.extra.fragment.ActivityResultLauncherMiddleWare
import nolambda.linkrouter.android.measure.DefaultMeasureConfig
import nolambda.linkrouter.android.measure.MeasuredAbstractAppRouter
import nolambda.linkrouter.android.middlewares.MiddleWareResult
import nolambda.linkrouter.android.middlewares.Middleware
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
    val heavyState: () -> String,
    val launcher: ActivityResultLauncher<Intent>? = null
)

private val logMiddleWare = object : Middleware<AppState> {
    override fun onRouting(route: BaseRoute<*>, routeParam: RouteParam<*, AppState>): MiddleWareResult<AppState> {
        routeParam.extra = AppState(
            isLoggedIn = false,
            heavyState = {
                Thread.sleep(1000)
                "This is a message"
            }
        )
        return MiddleWareResult(route, routeParam)
    }
}

@OptIn(AutoRegister::class)
object AppRouter : MeasuredAbstractAppRouter<AppState>(
    DefaultMeasureConfig(),
    logMiddleWare,
    ActivityResultLauncherMiddleWare { prev, launcher ->
        prev!!.copy(launcher = launcher)
    }
)

fun <P : Any, R> BaseRoute<P>.register(handler: RouteHandler<P, R, AppState>) {
    AppRouter.register(this, handler)
}