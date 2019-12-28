package nolambda.linkrouter.approuter

import nolambda.linkrouter.android.Route
import nolambda.linkrouter.android.RouteWithParam
import nolambda.linkrouter.optString

class AppRoutes {
    object Home : Route()
    object Cart : Route("app://cart")
    object Product : RouteWithParam<Product.ProductParam>(
        paths = arrayOf("app://product/{id}"),
        paramMapper = { ProductParam(it.optString("id", "")) }
    ) {
        data class ProductParam(
            val productId: String
        )
    }
}