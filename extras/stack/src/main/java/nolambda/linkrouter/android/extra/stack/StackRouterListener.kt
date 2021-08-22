package nolambda.linkrouter.android.extra.stack

interface StackRouterListener {
    fun shouldHandle(result: Any?): Boolean
    fun onAdd(item: StackRouterItem, result: Any?)
    fun onPop(item: StackRouterItem)
}