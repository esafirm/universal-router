package nolambda.linkrouter.android.measure

import nolambda.linkrouter.android.middlewares.MiddleWareChain
import nolambda.linkrouter.android.middlewares.Middleware

class MeasureMiddleWare<Extra>(
    private val measureConfig: MeasureConfig,
    private val middleWares: List<Middleware<Extra>>
) : Middleware<Extra> {
    override fun onRouting(chain: MiddleWareChain<Extra>): MiddleWareChain<Extra> {
        return middleWares.fold(chain) { acc, middleware ->
            measureConfig.doMeasure("$middleware") {
                middleware.onRouting(acc)
            }
        }
    }
}