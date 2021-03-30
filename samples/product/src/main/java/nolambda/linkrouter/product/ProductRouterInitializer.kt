package nolambda.linkrouter.product

import android.content.Context
import android.widget.Toast
import androidx.core.os.bundleOf
import nolambda.linkrouter.android.RouterInitializer
import nolambda.linkrouter.approuter.AppRoutes
import nolambda.linkrouter.approuter.register

class ProductRouterInitializer : RouterInitializer() {
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
