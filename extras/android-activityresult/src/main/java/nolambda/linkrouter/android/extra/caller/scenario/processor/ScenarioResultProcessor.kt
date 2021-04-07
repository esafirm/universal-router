package nolambda.linkrouter.android.extra.caller.scenario.processor

import androidx.activity.result.ActivityResult

interface ScenarioResultProcessor<R> {
    fun process(result: ActivityResult, lastResult: R?, onResult: OnResult<R>)
}