package nolambda.linkrouter.android.extra.fragment.scenario.processor

import androidx.activity.result.ActivityResult

interface ScenarioResultProcessor<R> {
    fun process(result: ActivityResult, lastResult: R?, onResult: OnResult<R>)
}