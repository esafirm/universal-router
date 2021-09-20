package nolambda.linkrouter

import nolambda.linkrouter.matcher.UriMatcher

class SimpleUriRouter<RES>(
    private val logger: UriRouterLogger? = null,
    dataHolder: MutableMap<DeepLinkEntry, EntryValue<RES>> = mutableMapOf()
) : UriRouter<RES>(logger) {

    private val entries: MutableMap<DeepLinkEntry, EntryValue<RES>> = dataHolder

    override fun resolveEntry(route: String): Pair<DeepLinkEntry, EntryValue<RES>>? {
        logger?.invoke("Entries size: ${entries.size}")

        return entries.asSequence().firstOrNull { entry ->
            val value = entry.value
            value.matcher.match(entry.key, route)
        }?.toPair()
    }

    override fun addEntry(vararg uri: String, matcher: UriMatcher, handler: UriRouterHandler<RES>) {
        val deepLinkEntries = uri.map { DeepLinkEntry.parse(it) }
        deepLinkEntries.forEach { entry ->
            entries[entry] = EntryValue(handler, matcher)
        }
    }

    override fun clear() {
        entries.clear()
    }
}