package nolambda.linkrouter.android

import java.util.*

interface StackRouterListener {
    fun shouldHandle(result: Any?): Boolean
    fun onAdd(item: StackRouterItem, result: Any?)
    fun onPop(item: StackRouterItem)
}

class StackAppRouter<Extra>(
    private val appRouter: AppRouter<Extra>,
    private val listener: StackRouterListener
) : AppRouter<Extra> by appRouter, StackRouter {

    private val stack: Stack<StackRouterItem> = Stack()

    init {
        addProcessor(listener::shouldHandle) { res, info ->
            val item = StackRouterItem(info.route, info.param)
            stack.push(item)
            listener.onAdd(item, res)
        }
    }

    override fun replace(item: StackRouterItem) {
        popStack()
        stack.push(item)
        pushCurrent()
    }

    override fun pop(): Boolean {
        if (stack.isEmpty()) return false

        popStack()
        return true
    }

    override fun popUntil(routeFinder: (StackRouterItem) -> Boolean): Boolean {
        // If stack is empty, don't do anything
        if (stack.isEmpty()) return false

        // If this is our current stack, don't do anything
        if (routeFinder(stack.peek())) return false

        while (!stack.isEmpty()) {
            val current = stack.pop()
            val found = routeFinder(current)
            if (found) {
                listener.onPop(current)
                break
            }
        }
        return true
    }

    private fun popStack(): StackRouterItem {
        val current = stack.pop()
        listener.onPop(current)
        return current
    }

    private fun pushCurrent() {
        if (stack.isEmpty().not()) {
            val item = stack.peek()
            if (item.route is Route) {
                appRouter.push(item.route)
            } else {
                @Suppress("UNCHECKED_CAST")
                appRouter.push(item.route as RouteWithParam<Any>, item.param as Any)
            }
        }
    }
}
