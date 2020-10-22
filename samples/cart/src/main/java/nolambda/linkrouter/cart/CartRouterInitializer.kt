package nolambda.linkrouter.cart

import android.content.Context
import android.widget.Toast
import nolambda.linkrouter.android.RouterInitializer
import nolambda.linkrouter.approuter.AppRoutes
import nolambda.linkrouter.approuter.register

class CartRouterInitializer : RouterInitializer() {
    override fun onInit(appContext: Context) {
        AppRoutes.Cart.register {
            val message = """
                Screen: Cart
                State: ${it.extra}
            """.trimIndent()
            Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show()
            CartScreen()
        }
    }
}