package nolambda.linkrouter.examples.picker

import androidx.activity.result.ActivityResult
import nolambda.linkrouter.android.Route
import nolambda.linkrouter.android.extra.fragment.scenario.ResultProcessor
import nolambda.linkrouter.android.extra.fragment.scenario.Scenario

object ResultPickerRoute : Route()

class PickerResultProcessor : ResultProcessor<String> {
    override fun process(result: ActivityResult): String {
        return result.data?.getStringExtra("result") ?: "empty"
    }
}

class ResultPickerScenario : Scenario<Unit, String>(ResultPickerRoute, PickerResultProcessor())