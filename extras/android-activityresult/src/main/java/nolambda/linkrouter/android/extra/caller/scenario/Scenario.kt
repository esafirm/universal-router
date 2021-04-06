package nolambda.linkrouter.android.extra.caller.scenario

import nolambda.linkrouter.android.BaseRoute
import nolambda.linkrouter.android.extra.caller.scenario.processor.ScenarioResultProcessor

abstract class Scenario<P : Any, R> {
    abstract val route: BaseRoute<P>
    abstract val processor: ScenarioResultProcessor<R>
}
