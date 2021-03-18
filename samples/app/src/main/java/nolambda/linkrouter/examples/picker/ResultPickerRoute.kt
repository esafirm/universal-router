package nolambda.linkrouter.examples.picker

import android.app.Activity
import androidx.activity.result.ActivityResult
import nolambda.linkrouter.android.Route
import nolambda.linkrouter.android.extra.fragment.RouteResultLauncher
import nolambda.linkrouter.android.extra.fragment.registerRouteForResult
import nolambda.linkrouter.android.extra.fragment.scenario.ActivityHost
import nolambda.linkrouter.android.extra.fragment.scenario.FragmentHost
import nolambda.linkrouter.android.extra.fragment.scenario.Scenario
import nolambda.linkrouter.android.extra.fragment.scenario.ScenarioHost
import nolambda.linkrouter.android.extra.fragment.scenario.processor.ComposedResultProcessor
import nolambda.linkrouter.android.extra.fragment.scenario.processor.OnResult
import nolambda.linkrouter.android.extra.fragment.scenario.processor.RetainedComposedResultProcessor
import nolambda.linkrouter.android.extra.fragment.scenario.processor.RetainedScenarioResultProcessor
import nolambda.linkrouter.android.extra.fragment.scenario.processor.ScenarioResultProcessor
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
        processors = listOf(CallbackResultProcessor(), CallbackResultProcessor())
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

abstract class AbsRetainedScenarioResultProcessor<R> : RetainedScenarioResultProcessor<R> {

    private var processorResult: R? = null

    protected fun setResult(result: R) {
        this.processorResult = result
    }

    override fun process(result: ActivityResult, lastResult: R?, onResult: OnResult<R>) {
        val currentResult = processorResult
        if (currentResult != null) {
            onResult.continueWith(currentResult)
        } else {
            onHandleProcess(result, lastResult, onResult)
        }
    }

    abstract fun onHandleProcess(result: ActivityResult, lastResult: R?, onResult: OnResult<R>)

    override fun onClear() {
        processorResult = null
    }
}

class CallbackResultProcessor : AbsRetainedScenarioResultProcessor<String>() {

    private lateinit var caller: RouteResultLauncher

    override fun onRegister(host: ScenarioHost, continuation: (ActivityResult) -> Unit) {
        val callback = { it: ActivityResult ->
            if (it.resultCode == Activity.RESULT_OK) {
                setResult("ABC")
            } else {
                setResult("CANCELLED")
            }
            continuation.invoke(it)
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
