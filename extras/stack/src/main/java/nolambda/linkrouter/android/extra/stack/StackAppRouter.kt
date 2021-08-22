package nolambda.linkrouter.android.extra.stack

import nolambda.linkrouter.android.AppRouter
import nolambda.linkrouter.android.BaseRoute
import nolambda.linkrouter.android.Route
import nolambda.linkrouter.android.RouteWithParam
import java.util.*

class StackAppRouter<Extra>(
    private val appRouter: AppRouter<Extra>,
    private val listener: StackRouterListener,
    private val idGenerator: ItemIdGenerator = UUIDItemIdGenerator()
) : AppRouter<Extra> by appRouter, StackRouter {

    private val stack: Stack<StackRouterItem> = Stack()

    init {
        addProcessor(listener::shouldHandle) { res, info ->
            val id = idGenerator.generate(info.route, info.param)
            val item = StackRouterItem(id, info.route, info.param)
            stack.push(item)
            listener.onAdd(item, res)
        }
    }

    override fun replace(route: BaseRoute<*>) {
        popStack()

        // TODO: need to make push only accept Route
        push(route as Route)
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
                appRouter.push(item.route as Route)
            } else {
                @Suppress("UNCHECKED_CAST")
                appRouter.push(item.route as RouteWithParam<Any>, item.param as Any)
            }
        }
    }
}
