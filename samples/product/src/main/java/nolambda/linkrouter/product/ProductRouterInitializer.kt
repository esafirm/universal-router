package nolambda.linkrouter.product

import android.content.Context
import android.widget.Toast
import androidx.core.os.bundleOf
import nolambda.linkrouter.android.RouteInit
import nolambda.linkrouter.annotations.Navigate
import nolambda.linkrouter.approuter.AppRoutes

@Navigate(route = AppRoutes.Product::class)
class ProductRouterInitializer : RouteInit {
    override fun onInit(appContext: Context) {
        AppRoutes.Product.register {
            Toast.makeText(appContext, "Product", Toast.LENGTH_SHORT).show()
            ProductScreen().apply {
                arguments = bundleOf(
                    "id" to it.param?.productId
                )
            }
        }
    }
}
