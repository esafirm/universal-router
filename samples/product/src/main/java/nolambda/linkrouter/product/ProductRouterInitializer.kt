package nolambda.linkrouter.product

import android.content.Context
import android.widget.Toast
import nolambda.linkrouter.android.Navigate
import nolambda.linkrouter.android.RouteInit
import nolambda.linkrouter.android.RouterInitializer
import nolambda.linkrouter.approuter.AppRoutes

class ProductRouterInitializer : RouterInitializer() {
    override fun onInit(appContext: Context) {
        AppRoutes.Product.register {
            Toast.makeText(appContext, "Product", Toast.LENGTH_SHORT).show()
            ProductScreen()
        }
    }
}

@Navigate(route = AppRoutes.Product::class)
class SomethingRouter : RouteInit {
    override fun onInit(appContext: Context) {
        Toast.makeText(appContext, "Product", Toast.LENGTH_SHORT).show()
        ProductScreen()
    }
}