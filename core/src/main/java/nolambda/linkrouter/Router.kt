package nolambda.linkrouter

interface Router<REQ, RES> {
    fun clear()
    fun resolve(param: REQ): RES
}

typealias RouterHandler<T> = (Any) -> T

abstract class SimpleRouter<RES> : Router<Any, RES> {

    private var entries = linkedMapOf<Any, RouterHandler<RES>>()

    fun addEntry(param: Any, handler: RouterHandler<RES>) {
        entries[param] = handler
    }

    override fun clear() {
        entries.clear()
    }

    override fun resolve(param: Any): RES {
        val entry = entries[param] ?: throw IllegalStateException("No entry for parameter $param")
        return entry.invoke(param)
    }
}