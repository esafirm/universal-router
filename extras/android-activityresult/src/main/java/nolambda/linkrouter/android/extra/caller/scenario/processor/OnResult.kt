package nolambda.linkrouter.android.extra.caller.scenario.processor

interface OnResult<R> {
    fun continueWith(result: R)
}