package nolambda.linkrouter

import nolambda.linkrouter.matcher.UriMatcher
import java.util.concurrent.ConcurrentHashMap

class KeyUriRouter<URI>(
    logger: UriRouterLogger? = null,
    private val keyExtractor: (DeepLinkEntry) -> String
) : UriRouter<URI>(logger) {

    private val entryContainer = ConcurrentHashMap<String, MutableList<DeepLinkEntry>>()

    override fun clear() {
        super.clear()
        entryContainer.clear()
    }

    override fun resolveEntry(route: String): Pair<DeepLinkEntry, EntryValue<URI>>? {
        val entry = DeepLinkEntry.parse(route)
        val key = keyExtractor(entry)
        val list = entryContainer[key]
        val actualKey = list?.firstOrNull { it.matches(route) }
        if (actualKey != null) {
            return actualKey to entries[actualKey]!!
        }
        return null
    }

    override fun addEntry(vararg uri: String, matcher: UriMatcher, handler: UriRouterHandler<URI>) {
        val deepLinkEntries = uri.map { DeepLinkEntry.parse(it) }
        deepLinkEntries.forEach { entry ->
            entries[entry] = EntryValue(handler, matcher)

            val key = keyExtractor(entry)
            val entriesHolder = entryContainer.getOrPut(key) { mutableListOf() }
            entriesHolder as MutableList<DeepLinkEntry>
            entriesHolder.add(entry)
        }
    }
}