package nolambda.linkrouter.android.extra.fragment.scenario.processor

interface OnResult<R> {
    fun continueWith(result: R)
}