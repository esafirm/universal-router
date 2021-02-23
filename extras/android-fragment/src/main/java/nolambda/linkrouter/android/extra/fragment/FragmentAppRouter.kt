package nolambda.linkrouter.android.extra.fragment

import androidx.fragment.app.Fragment
import nolambda.linkrouter.android.AbstractAppRouter
import nolambda.linkrouter.android.RouteResult
import nolambda.linkrouter.android.RouteWithParam
import nolambda.linkrouter.android.RouterProcessor

class FragmentAppRouter<Extra>(
    private val appRouter: AbstractAppRouter<Extra>,
    private val fragment: Fragment
) : RouterProcessor<Extra> by appRouter {
    override fun <P : Any> push(route: RouteWithParam<P>, param: P): RouteResult {
        return appRouter.push(FragmentProcessor, FragmentProcessor.Param(
            fragment,
            param,
            route
        ))
    }
}
