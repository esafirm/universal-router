package nolambda.linkrouter.android.extra.fragment.scenario

import androidx.activity.result.ActivityResult
import nolambda.linkrouter.android.BaseRoute

open class Scenario<P : Any, R>(
    val route: BaseRoute<P>,
    val processor: ResultProcessor<R>
)

interface ResultProcessor<R> {
    fun process(result: ActivityResult): R
}
