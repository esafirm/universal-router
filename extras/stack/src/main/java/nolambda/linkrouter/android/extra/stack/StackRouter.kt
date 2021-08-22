package nolambda.linkrouter.android.extra.stack

import nolambda.linkrouter.android.BaseRoute

interface StackRouter {
    fun replace(route: BaseRoute<*>)
    fun pop(): Boolean
    fun popUntil(routeFinder: (StackRouterItem) -> Boolean): Boolean
}