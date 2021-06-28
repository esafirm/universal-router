package nolambda.linkrouter

import nolambda.linkrouter.DeepLinkUri.Companion.toDeepLinkUri
import nolambda.linkrouter.matcher.DeepLinkEntryMatcher
import nolambda.linkrouter.matcher.UriMatcher
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.Future

typealias UriRouterLogger = (String) -> Unit
typealias UriRouterHandler<T> = (DeepLinkUri, Map<String, String>) -> T

class EntryValue<T>(
    val handler: UriRouterHandler<T>,
    val matcher: UriMatcher
)
typealias Row<RES> = Pair<DeepLinkEntry, EntryValue<RES>>

abstract class UriRouter<RES>(
    private val logger: UriRouterLogger?
) : Router<String, RES?> {

    internal var entries = ConcurrentHashMap<DeepLinkEntry, EntryValue<RES>>()
    private val deeplinkMap = ConcurrentHashMap<String, MutableList<DeepLinkEntry>>()

    override fun clear() {
        entries.clear()
    }

    private fun parallelResolve(route: String): Pair<DeepLinkEntry, EntryValue<RES>>? {
        val futures = mutableListOf<Future<*>>()
        val dispatcher = Executors.newFixedThreadPool(2)
        val entriesCollection = entries.toList().chunked(entries.size / 2)

        entriesCollection.forEach { pair ->
            val future = dispatcher.submit<Row<RES>> {
                pair.firstOrNull {
                    it.second.matcher.match(it.first, route)
                }
            }
            futures.add(future)
        }

        @Suppress("UNCHECKED_CAST")
        return futures.firstOrNull { it.get() != null }?.get() as Row<RES>
    }

    private fun syncResolve(route: String): Pair<DeepLinkEntry, EntryValue<RES>>? {
        return entries.toList().firstOrNull {
            it.second.matcher.match(it.first, route)
        }
    }

    private fun containerResolve(route: String): Pair<DeepLinkEntry, EntryValue<RES>>? {
        val parsed = DeepLinkEntry.parse(route)
        val containerKey = "${parsed.uri.scheme}${parsed.uri.host}"
        val list = deeplinkMap[containerKey]
        val key = list?.firstOrNull { it.matches(route) }
        if (key != null) {
            return key to entries[key]!!
        }
        return null
    }

    abstract fun resolveEntry(route: String): Pair<DeepLinkEntry, EntryValue<RES>>?

    override fun resolve(route: String): RES? {
        logger?.invoke("Entries size: ${entries.size}")

        val result = resolveEntry(route)
        if (result == null) {
            logger?.invoke("Path not implemented $route")
            return null
        }

        val deepLinkEntry = result.first
        val value = result.second

        val deepLinkUri = route.toDeepLinkUri()
        val parameters = deepLinkEntry.getParameters(deepLinkUri)

        return value.handler.invoke(deepLinkUri, parameters)
    }

    open fun addEntry(
        vararg uri: String,
        matcher: UriMatcher = DeepLinkEntryMatcher,
        handler: UriRouterHandler<RES>
    ) {
        val deepLinkEntries = uri.map { DeepLinkEntry.parse(it) }
        deepLinkEntries.forEach { entry ->
            entries[entry] = EntryValue(handler, matcher)
        }
    }
}
