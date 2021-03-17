package nolambda.linkrouter.android.extra.fragment.scenario.processor

import androidx.activity.result.ActivityResult

class ComposedResultProcessor<R>(
    private val processors: List<ScenarioResultProcessor<R>>
) : ScenarioResultProcessor<R> {
    override fun process(result: ActivityResult, onResult: (R) -> Unit) {
        TODO("Not yet implemented")
    }
}