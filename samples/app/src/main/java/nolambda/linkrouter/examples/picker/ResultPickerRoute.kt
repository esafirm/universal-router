package nolambda.linkrouter.examples.picker

import android.app.Activity
import androidx.activity.result.ActivityResult
import nolambda.linkrouter.android.Route
import nolambda.linkrouter.android.extra.caller.RouteResultLauncher
import nolambda.linkrouter.android.extra.caller.registerRouteForResult
import nolambda.linkrouter.android.extra.caller.scenario.ActivityHost
import nolambda.linkrouter.android.extra.caller.scenario.FragmentHost
import nolambda.linkrouter.android.extra.caller.scenario.Scenario
import nolambda.linkrouter.android.extra.caller.scenario.ScenarioHost
import nolambda.linkrouter.android.extra.caller.scenario.processor.ComposedResultProcessor
import nolambda.linkrouter.android.extra.caller.scenario.processor.OnResult
import nolambda.linkrouter.android.extra.caller.scenario.processor.RetainedComposedResultProcessor
import nolambda.linkrouter.android.extra.caller.scenario.processor.RetainedScenarioResultProcessor
import nolambda.linkrouter.android.extra.caller.scenario.processor.ScenarioResultProcessor
import nolambda.linkrouter.approuter.AppRouter

object ResultPickerRoute : Route()

class PickerResultProcessor : ScenarioResultProcessor<String> {
    override fun process(result: ActivityResult, lastResult: String?, onResult: OnResult<String>) {
        val text = result.data?.getStringExtra("result") ?: "empty"
        onResult.continueWith(text)
    }
}

class ResultPickerScenario : Scenario<Unit, String>() {
    override val route = ResultPickerRoute
    override val processor = RetainedComposedResultProcessor(
        processors = listOf(PickAgainResultProcessor(), PickAgainResultProcessor())
    )
}

class SimpleResultPickerScenario : Scenario<Unit, String>() {
    override val route = ResultPickerRoute
    override val processor = ComposedResultProcessor(
        processors = listOf(SimpleResultProcessor("1"), SimpleResultProcessor("2"))
    )
}

/* --------------------------------------------------- */
/* > Test */
/* --------------------------------------------------- */

class SimpleResultProcessor(private val data: String) : ScenarioResultProcessor<String> {
    override fun process(result: ActivityResult, lastResult: String?, onResult: OnResult<String>) {
        if (lastResult == null) {
            onResult.continueWith(data)
        } else {
            onResult.continueWith("${lastResult}${data}")
        }
    }
}

abstract class CallbackScenarioResultProcessor<R> : RetainedScenarioResultProcessor<R> {

    private var processorResult: R? = null
    private lateinit var theContinuation: (ActivityResult) -> Unit

    override fun onRegister(host: ScenarioHost, continuation: (ActivityResult) -> Unit) {
        theContinuation = continuation
        onPrepareCaller(host, object : OnResult<R> {
            override fun continueWith(result: R) {
                processorResult = result
            }
        })
    }

    override fun process(result: ActivityResult, lastResult: R?, onResult: OnResult<R>) {
        val currentResult = processorResult
        if (currentResult != null) {
            onResult.continueWith(currentResult)
        } else {
            onHandleProcess(result, lastResult, onResult)
        }
    }

    abstract fun onPrepareCaller(host: ScenarioHost, onResult: OnResult<R>)
    abstract fun onHandleProcess(result: ActivityResult, lastResult: R?, onResult: OnResult<R>)

    override fun onClear() {
        processorResult = null
    }
}

class PickAgainResultProcessor : CallbackScenarioResultProcessor<String>() {

    private lateinit var caller: RouteResultLauncher

    override fun onPrepareCaller(host: ScenarioHost, onResult: OnResult<String>) {
        val callback = { it: ActivityResult ->
            val result = if (it.resultCode == Activity.RESULT_OK) {
                "ABC"
            } else {
                "CANCELLED"
            }
            onResult.continueWith(result)
        }
        caller = when (host) {
            is ActivityHost -> host.activity.registerRouteForResult(ResultPickerRoute, callback)
            is FragmentHost -> host.fragment.registerRouteForResult(ResultPickerRoute, callback)
        }
    }

    override fun onHandleProcess(result: ActivityResult, lastResult: String?, onResult: OnResult<String>) {
        if (result.resultCode == Activity.RESULT_OK) {
            caller(AppRouter)
        } else {
            onResult.continueWith("CANCELLED")
        }
    }
}
