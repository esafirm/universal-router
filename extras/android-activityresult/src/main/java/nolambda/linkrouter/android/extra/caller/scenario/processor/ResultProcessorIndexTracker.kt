package nolambda.linkrouter.android.extra.caller.scenario.processor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

internal class ResultProcessorIndexTracker(saveState: SavedStateHandle) : ViewModel() {
    companion object {
        const val FIRST_INDEX = 0
    }

    var index: Int = saveState.get<Int>("index") ?: FIRST_INDEX

    fun reset() {
        index = FIRST_INDEX
    }
}