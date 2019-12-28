package nolambda.linkrouter.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_cart.*
import nolambda.linkrouter.android.Router
import nolambda.linkrouter.approuter.AppRoutes

class CartScreen : Fragment(R.layout.fragment_cart) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnGoHome.setOnClickListener {
            Router.push(AppRoutes.Home)
        }
    }
}