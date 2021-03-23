package nolambda.linkrouter.android.extra.fragment.scenario.processor

import androidx.activity.result.ActivityResult

class ComposedResultProcessor<R>(
    private val processors: List<ScenarioResultProcessor<R>>
) : ScenarioResultProcessor<R> {

    init {
        check(processors.isNotEmpty()) {
            "Processors cannot be empty!"
        }
    }

    override fun process(result: ActivityResult, lastResult: R?, onResult: OnResult<R>) {
        callProcessor(0, result, lastResult, onResult)
    }

    private fun callProcessor(
        index: Int,
        activityResult: ActivityResult,
        lastResult: R?,
        onResult: OnResult<R>
    ) {
        val currentProcessor = processors[index]
        currentProcessor.process(activityResult, lastResult, object : OnResult<R> {
            override fun continueWith(result: R) {
                val nextIndex = index + 1
                val nextProcessor = processors.getOrNull(nextIndex)
                if (nextProcessor == null) {
                    onResult.continueWith(result)
                    return
                }
                callProcessor(nextIndex, activityResult, result, onResult)
            }
        })
    }
}
