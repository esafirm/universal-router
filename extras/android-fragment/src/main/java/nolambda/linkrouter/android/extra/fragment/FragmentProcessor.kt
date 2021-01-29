package nolambda.linkrouter.android.extra.fragment

import androidx.fragment.app.Fragment
import nolambda.linkrouter.android.RouteWithParam

object FragmentProcessor : RouteWithParam<FragmentProcessor.Param>() {
    data class Param(
        val fragment: Fragment,
        val originalParam: Any,
        val originalRoute: RouteWithParam<*>
    )
}
