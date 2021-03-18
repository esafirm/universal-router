package nolambda.linkrouter.examples.picker

import androidx.activity.result.ActivityResult
import nolambda.linkrouter.android.Route
import nolambda.linkrouter.android.extra.fragment.RouteResultLauncher
import nolambda.linkrouter.android.extra.fragment.registerRouteForResult
import nolambda.linkrouter.android.extra.fragment.scenario.ActivityHost
import nolambda.linkrouter.android.extra.fragment.scenario.FragmentHost
import nolambda.linkrouter.android.extra.fragment.scenario.Scenario
import nolambda.linkrouter.android.extra.fragment.scenario.ScenarioHost
import nolambda.linkrouter.android.extra.fragment.scenario.processor.RetainedComposedResultProcessor
import nolambda.linkrouter.android.extra.fragment.scenario.processor.RetainedScenarioResultProcessor
import nolambda.linkrouter.android.extra.fragment.scenario.processor.ScenarioResultProcessor
import nolambda.linkrouter.approuter.AppRouter

object ResultPickerRoute : Route()

class PickerResultProcessor : ScenarioResultProcessor<String> {
    override fun process(result: ActivityResult, onResult: (String) -> Unit) {
        onResult(result.data?.getStringExtra("result") ?: "empty")
    }
}

class ResultPickerScenario : Scenario<Unit, String>() {
    override val route = ResultPickerRoute
    override val processor = RetainedComposedResultProcessor(
        processors = listOf(A(), A())
    )
}

/* --------------------------------------------------- */
/* > Test */
/* --------------------------------------------------- */

class A : RetainedScenarioResultProcessor<String> {

    private var theResult: String? = null
    private lateinit var caller: RouteResultLauncher

    override fun process(result: ActivityResult, onResult: (String) -> Unit) {
        if (theResult != null) {
            onResult(theResult!!)
        } else {
            caller(AppRouter)
        }
    }

    override fun onRegister(host: ScenarioHost, continuation: (ActivityResult) -> Unit) {
        val callback = { it: ActivityResult ->
            theResult = "ABC"
            continuation.invoke(it)
        }
        caller = when (host) {
            is ActivityHost -> host.activity.registerRouteForResult(ResultPickerRoute, callback)
            is FragmentHost -> host.fragment.registerRouteForResult(ResultPickerRoute, callback)
        }
    }
}