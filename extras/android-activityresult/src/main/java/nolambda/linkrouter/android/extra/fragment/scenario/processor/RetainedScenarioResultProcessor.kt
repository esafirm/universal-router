package nolambda.linkrouter.android.extra.fragment.scenario.processor

import androidx.activity.result.ActivityResult
import nolambda.linkrouter.android.extra.fragment.scenario.ScenarioHost

interface RetainedScenarioResultProcessor<R> : ScenarioResultProcessor<R> {
    fun onRegister(host: ScenarioHost, continuation: (ActivityResult) -> Unit)
}