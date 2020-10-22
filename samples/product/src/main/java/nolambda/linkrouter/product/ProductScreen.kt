package nolambda.linkrouter.product

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_product.*
import nolambda.linkrouter.approuter.AppRouter
import nolambda.linkrouter.approuter.AppRoutes

class ProductScreen : Fragment(R.layout.fragment_product) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productId = arguments?.getString("id")
        txtProductId.text = "Product ID: $productId"

        btnGoCart.setOnClickListener {
            AppRouter.push(AppRoutes.Cart)
        }

        btnGoCartWithUrl.setOnClickListener {
            AppRouter.goTo("app://bktk.link/cart")
        }
    }
}