package nolambda.linkrouter

abstract class Router<T> {

    internal var entries = linkedMapOf<DeepLinkEntry, RouterHandler<T>>()

    abstract fun goTo(uri: String)
    fun resolve(uri: String): T {
        val filteredMap = entries.filter { it.key.matches(uri) }
        if (filteredMap.isEmpty()) {
            throw IllegalStateException("Path not implemented $uri")
        }
        val deepLinkEntry = filteredMap.keys.first()
        val handler = filteredMap[deepLinkEntry]

        return handler!!.invoke(deepLinkEntry.getParameters(uri))
    }
}

typealias RouterHandler<T> = (Map<String, String>) -> T

fun <T> Router<T>.addEntry(vararg uri: String, handler: RouterHandler<T>) {
    val deepLinkEntries = uri.map { DeepLinkEntry.parse(it) }
    deepLinkEntries.forEach {
        entries[it] = handler
    }
}
