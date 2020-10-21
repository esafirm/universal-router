package nolambda.linkrouter.approuter

import nolambda.linkrouter.DeepLinkUri
import nolambda.linkrouter.android.AbstractAppRouter
import nolambda.linkrouter.android.BaseRoute
import nolambda.linkrouter.android.Route
import nolambda.linkrouter.android.RouteHandler
import nolambda.linkrouter.android.RouteWithParam
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

object AppRouter : AbstractAppRouter<Nothing>()

fun <P : Any, R> BaseRoute<P>.register(handler: RouteHandler<P, R, Nothing>) {
    AppRouter.register(this, handler)
}