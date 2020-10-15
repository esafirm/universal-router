package nolambda.linkrouter

import nolambda.linkrouter.DeepLinkUri.Companion.toDeepLinkUri

typealias UriRouterHandler<T> = (DeepLinkUri, Map<String, String>) -> T

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

        val deepLinkUri = param.toDeepLinkUri()
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