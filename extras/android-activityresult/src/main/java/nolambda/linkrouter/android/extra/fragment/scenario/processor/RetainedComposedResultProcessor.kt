package nolambda.linkrouter.android.extra.fragment.scenario.processor

import androidx.activity.result.ActivityResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import nolambda.linkrouter.android.extra.fragment.scenario.ScenarioHost
import nolambda.linkrouter.android.extra.fragment.scenario.lifecycle
import nolambda.linkrouter.android.extra.fragment.scenario.viewModelStore

class RetainedComposedResultProcessor<R>(
    private val processors: List<RetainedScenarioResultProcessor<R>>
) : RetainedScenarioResultProcessor<R> {

    private lateinit var tracker: ResultProcessorIndexTracker

    init {
        check(processors.isNotEmpty()) {
            "Composed processors cannot be empty!"
        }
    }

    override fun process(result: ActivityResult, onResult: (R) -> Unit) {
        val firstIndex = tracker.index
        callProcessors(firstIndex, result, onResult)
    }

    private fun callProcessors(
        index: Int,
        result: ActivityResult,
        onResult: (R) -> Unit
    ) {
        val currentProcessor = processors[index]
        currentProcessor.process(result) { lastResult ->
            val nextIndex = index + 1
            val nextProcessor = processors.getOrNull(nextIndex)
            if (nextProcessor == null) {
                // call last callback
                onResult.invoke(lastResult)
                // reset the retain index
                tracker.reset()
                return@process
            }

            callProcessors(nextIndex, result, onResult)
            tracker.index = index
        }
    }

    override fun onRegister(host: ScenarioHost, continuation: (ActivityResult) -> Unit) {
        host.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_CREATE) {
                    tracker = ViewModelProvider(host.viewModelStore).get(ResultProcessorIndexTracker::class.java)
                }
            }
        })

        processors.forEach {
            it.onRegister(host, continuation)
        }
    }
}
