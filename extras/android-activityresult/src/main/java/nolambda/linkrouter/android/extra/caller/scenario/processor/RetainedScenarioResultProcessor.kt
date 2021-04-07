package nolambda.linkrouter.android.extra.caller.scenario.processor

import androidx.activity.result.ActivityResult
import nolambda.linkrouter.android.extra.caller.scenario.ScenarioHost

interface RetainedScenarioResultProcessor<R> : ScenarioResultProcessor<R> {
    fun onRegister(host: ScenarioHost, continuation: (ActivityResult) -> Unit)
    fun onClear()
}
