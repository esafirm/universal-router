package nolambda.linkrouter

import nolambda.linkrouter.error.RouteNotFoundException

internal interface Router<REQ, RES> {
    fun clear()
    fun resolve(route: REQ): RES
}

typealias RouterHandler<T> = (Any) -> T

abstract class SimpleRouter<RES> : Router<Any, RES> {

    private var entries = linkedMapOf<Any, RouterHandler<RES>>()

    fun addEntry(route: Any, handler: RouterHandler<RES>) {
        entries[route] = handler
    }

    override fun clear() {
        entries.clear()
    }

    override fun resolve(route: Any): RES {
        val entry = entries[route] ?: throw RouteNotFoundException(route)
        return entry.invoke(route)
    }
}