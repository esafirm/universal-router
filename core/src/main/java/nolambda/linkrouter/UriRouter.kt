package nolambda.linkrouter

typealias UriRouterHandler<T> = (Map<String, String>) -> T

abstract class UriRouter<RES> : Router<String, RES> {

    internal var entries = linkedMapOf<DeepLinkEntry, UriRouterHandler<RES>>()

    override fun clear() {
        entries.clear()
    }

    override fun resolve(param: String): RES {
        val filteredMap = entries.filter { it.key.matches(param) }
        if (filteredMap.isEmpty()) {
            throw IllegalStateException("Path not implemented $param")
        }
        val deepLinkEntry = filteredMap.keys.first()
        val handler = filteredMap[deepLinkEntry]

        return handler!!.invoke(deepLinkEntry.getParameters(param))
    }
}

fun <T> UriRouter<T>.addEntry(vararg uri: String, handler: UriRouterHandler<T>) {
    val deepLinkEntries = uri.map { DeepLinkEntry.parse(it) }
    deepLinkEntries.forEach {
        entries[it] = handler
    }
}