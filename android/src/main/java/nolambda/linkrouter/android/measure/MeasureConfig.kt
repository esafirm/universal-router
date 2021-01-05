package nolambda.linkrouter.android.measure

interface MeasureConfig {
    fun shouldMeasure(name: String): Boolean
    fun <T> doMeasure(name: String, operation: () -> T): T
}