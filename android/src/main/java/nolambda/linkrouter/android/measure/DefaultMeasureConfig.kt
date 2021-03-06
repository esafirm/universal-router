package nolambda.linkrouter.android.measure

import nolambda.linkrouter.android.RouterPlugin
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

class DefaultMeasureConfig : MeasureConfig {
    override fun shouldMeasure(name: String): Boolean = true
    override fun <T> doMeasure(name: String, operation: () -> T): T {
        var returnValue: T
        val time = measureNanoTime {
            returnValue = operation()
        }
        RouterPlugin.logger?.invoke("[Measure] [$name] - $time ns")
        return returnValue
    }
}
