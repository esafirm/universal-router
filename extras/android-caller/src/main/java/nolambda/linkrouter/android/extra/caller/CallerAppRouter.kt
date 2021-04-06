package nolambda.linkrouter.android.extra.caller

import nolambda.linkrouter.android.AbstractAppRouter
import nolambda.linkrouter.android.Route
import nolambda.linkrouter.android.RouteResult
import nolambda.linkrouter.android.RouteWithParam
import nolambda.linkrouter.android.RouterProcessor

class CallerAppRouter<Extra>(
    private val appRouter: AbstractAppRouter<Extra>,
    private val caller: Any
) : RouterProcessor<Extra> by appRouter {
    override fun push(route: Route): RouteResult {
        return appRouter.push(CallerProcessor, CallerProcessor.Param(
            caller = caller,
            originalRoute = route
        ))
    }

    override fun <P : Any> push(route: RouteWithParam<P>, param: P): RouteResult {
        return appRouter.push(CallerProcessor, CallerProcessor.Param(
            caller,
            param,
            route
        ))
    }
}
