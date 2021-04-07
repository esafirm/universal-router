package nolambda.linkrouter.android.extra.caller.scenario

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import nolambda.linkrouter.android.Route
import nolambda.linkrouter.android.RouteWithParam
import nolambda.linkrouter.android.extra.caller.createLauncher
import nolambda.linkrouter.android.extra.caller.scenario.launcher.ParameterizedScenarioLauncher
import nolambda.linkrouter.android.extra.caller.scenario.launcher.ScenarioLauncher
import nolambda.linkrouter.android.extra.caller.scenario.processor.OnResult
import nolambda.linkrouter.android.extra.caller.scenario.processor.RetainedScenarioResultProcessor

fun <P : Any, R> ComponentActivity.registerScenarioForResult(
    scenario: Scenario<P, R>,
    onCallback: (R) -> Unit
): ParameterizedScenarioLauncher<P> {
    val host = ActivityHost(this)
    val internalLauncher = createInternalLauncher(host, scenario, onCallback)
    return ParameterizedScenarioLauncher(internalLauncher, scenario.route as RouteWithParam<P>)
}

fun <R> ComponentActivity.registerScenarioForResult(
    scenario: Scenario<Unit, R>,
    onCallback: (R) -> Unit
): ScenarioLauncher {
    val host = ActivityHost(this)
    val internalLauncher = createInternalLauncher(host, scenario, onCallback)
    return ScenarioLauncher(internalLauncher, scenario.route as Route)
}

fun <P : Any, R> Fragment.registerScenarioForResult(
    scenario: Scenario<P, R>,
    onCallback: (R) -> Unit
): ParameterizedScenarioLauncher<P> {
    val host = FragmentHost(this)
    val internalLauncher = createInternalLauncher(host, scenario, onCallback)
    return ParameterizedScenarioLauncher(internalLauncher, scenario.route as RouteWithParam<P>)
}

fun <R> Fragment.registerScenarioForResult(
    scenario: Scenario<Unit, R>,
    onCallback: (R) -> Unit
): ScenarioLauncher {
    val host = FragmentHost(this)
    val internalLauncher = createInternalLauncher(host, scenario, onCallback)
    return ScenarioLauncher(internalLauncher, scenario.route as Route)
}

private fun <R> createInternalLauncher(
    host: ScenarioHost,
    scenario: Scenario<*, R>,
    onCallback: (R) -> Unit
): ActivityResultLauncher<Intent> {
    val processor = scenario.processor

    val continuation = { activityResult: ActivityResult ->
        processor.process(activityResult, null, object : OnResult<R> {
            override fun continueWith(result: R) {
                onCallback(result)

                if (processor is RetainedScenarioResultProcessor<R>) {
                    processor.onClear()
                }
            }
        })
    }
    if (processor is RetainedScenarioResultProcessor<R>) {
        processor.onRegister(host, continuation)
    }
    return host.createLauncher(continuation)
}

private fun ScenarioHost.createLauncher(callback: (ActivityResult) -> Unit) = when (this) {
    is ActivityHost -> activity.createLauncher(callback)
    is FragmentHost -> fragment.createLauncher(callback)
}
