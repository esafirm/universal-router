package nolambda.linkrouter

import nolambda.linkrouter.DeepLinkUri.Companion.toDeepLinkUri
import nolambda.linkrouter.matcher.DeepLinkEntryMatcher
import nolambda.linkrouter.matcher.UriMatcher
import java.util.concurrent.ConcurrentHashMap

typealias UriRouterLogger = (String) -> Unit
typealias UriRouterHandler<T> = (DeepLinkUri, Map<String, String>) -> T

class EntryValue<T>(
    val handler: UriRouterHandler<T>,
    val matcher: UriMatcher
)

abstract class UriRouter<RES>(
    private val logger: UriRouterLogger?
) : Router<String, RES?> {

    internal val entries = ConcurrentHashMap<DeepLinkEntry, EntryValue<RES>>()

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

    override fun clear() {
        entries.clear()
    }
}
