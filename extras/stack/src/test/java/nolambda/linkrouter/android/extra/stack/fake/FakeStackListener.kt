package nolambda.linkrouter.android.extra.stack.fake

import nolambda.linkrouter.android.extra.stack.StackRouterItem
import nolambda.linkrouter.android.extra.stack.StackRouterListener
import java.util.*

class FakeStackListener(
    private val shouldHandle: Boolean = true
) : StackRouterListener {

    val stack: Stack<Any> = Stack()

    override fun shouldHandle(result: Any?): Boolean {
        return shouldHandle
    }

    override fun onAdd(item: StackRouterItem, result: Any?) {
        stack.add(result)
    }

    override fun onPop(item: StackRouterItem) {
        stack.pop()
    }
}