package nolambda.linkrouter.android

typealias RouteProcessor<T> = (T, ActionInfo) -> Unit

inline fun <reified T> RouterComponents.addTypeProcessor(noinline processor: RouteProcessor<T>) {
    val expectedClass = T::class.java
    val canHandle = { result: Any? ->
        when (result) {
            null -> false
            else -> {
                val clazz = result.javaClass
                expectedClass.isAssignableFrom(clazz)
            }
        }
    }
    addProcessor(canHandle, processor)
}