package nolambda.linkrouter.android

class RouteResult(
    val isHandled: Boolean,
    private val result: Any? = null
) {
    @Suppress("UNCHECKED_CAST")
    fun <R> getResultOrError(): R = result as R

    fun <R> getResultOrNull(): R? {
        return try {
            @Suppress("UNCHECKED_CAST")
            result as? R?
        } catch (e: Exception) {
            null
        }
    }
}