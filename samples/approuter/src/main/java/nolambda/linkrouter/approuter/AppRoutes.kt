package nolambda.linkrouter.approuter

import nolambda.linkrouter.android.Route
import nolambda.linkrouter.android.RouteWithParam
import nolambda.linkrouter.optString

class AppRoutes {
    object Home : Route()
    object Cart : Route("app://cart")
    object Product : RouteWithParam<Product.ProductParam>(
        "app://product/{id}"
    ) {
        data class ProductParam(
            val productId: String
        )

        override fun mapParameter(raw: Map<String, String>): ProductParam {
            return ProductParam(raw.optString("id", ""))
        }
    }
}