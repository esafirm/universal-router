package nolambda.linkrouter

import nolambda.linkrouter.DeepLinkUri.Companion.toDeepLinkUri

typealias UriRouterHandler<T> = (DeepLinkUri, Map<String, String>) -> T

abstract class UriRouter<RES>(
    private val logger: ((String) -> Unit)? = null
) : Router<String, RES?> {

    internal var entries = linkedMapOf<DeepLinkEntry, UriRouterHandler<RES>>()

    override fun clear() {
        entries.clear()
    }

    override fun resolve(route: String): RES? {
        val filteredMap = entries.filter { it.key.matches(route) }
        if (filteredMap.isEmpty()) {
            logger?.invoke("Path not implemented $route")
            return null
        }
        val deepLinkEntry = filteredMap.keys.first()
        val handler = filteredMap[deepLinkEntry]

        val deepLinkUri = route.toDeepLinkUri()
        val parameters = deepLinkEntry.getParameters(deepLinkUri)
        return handler!!.invoke(deepLinkUri, parameters)
    }
}

fun <T> UriRouter<T>.addEntry(vararg uri: String, handler: UriRouterHandler<T>) {
    val deepLinkEntries = uri.map { DeepLinkEntry.parse(it) }
    deepLinkEntries.forEach {
        entries[it] = handler
    }
}