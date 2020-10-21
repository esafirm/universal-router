package nolambda.linkrouter.examples.notsimple

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_sample_home.*
import nolambda.linkrouter.approuter.AppRouter
import nolambda.linkrouter.approuter.AppRoutes
import nolambda.linkrouter.examples.R

class HomeScreen : Fragment(R.layout.fragment_sample_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnGoToProductOne.setOnClickListener {
            AppRouter.push(AppRoutes.Product, AppRoutes.Product.ProductParam("123"))
        }
        btnGoToProductTwo.setOnClickListener {
            AppRouter.goTo("https://m.bukatoko.com/123")
        }
    }
}