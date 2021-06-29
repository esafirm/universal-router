package nolambda.linkrouter

import nolambda.linkrouter.matcher.DeepLinkEntryMatcher
import nolambda.linkrouter.matcher.UriMatcher
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet

class KeyUriRouter<URI>(
    logger: UriRouterLogger? = null,
    private val keyExtractor: (DeepLinkEntry) -> String
) : UriRouter<URI>(logger) {

    private val entryContainer = ConcurrentHashMap<String, MutableSet<DeepLinkEntry>>()

    override fun clear() {
        super.clear()
        entryContainer.clear()
    }

    override fun resolveEntry(route: String): Pair<DeepLinkEntry, EntryValue<URI>>? {
        val entry = DeepLinkEntry.parse(route)
        val key = keyExtractor(entry)
        val list = entryContainer[key]

        val actualKey = list?.firstOrNull { it.matches(route) } ?: return null

        val entryValue = entries[actualKey]!!
        if (entryValue.matcher == DeepLinkEntryMatcher) {
            return actualKey to entryValue
        }
        // to support custom matcher
        return if (entryValue.matcher.match(actualKey, route)) {
            return actualKey to entryValue
        } else null
    }

    override fun addEntry(
        vararg uri: String,
        matcher: UriMatcher,
        handler: UriRouterHandler<URI>
    ) {
        val deepLinkEntries = uri.map { DeepLinkEntry.parse(it) }
        deepLinkEntries.forEach { entry ->
            entries[entry] = EntryValue(handler, matcher)

            val key = keyExtractor(entry)
            inputToEntryContainer(key, entry)
        }
    }

    private fun inputToEntryContainer(key: String, entry: DeepLinkEntry) {
        val entriesHolder = entryContainer.getOrPut(key) { CopyOnWriteArraySet() }
        entriesHolder.add(entry)
    }
}