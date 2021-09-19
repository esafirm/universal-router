package nolambda.linkrouter

import nolambda.linkrouter.DeepLinkUri.Companion.toDeepLinkUri
import nolambda.linkrouter.matcher.UriMatcher

/**
 * router that use "key" to group [DeepLinkUri] or [DeepLinkEntry] for faster
 * register and lookup
 *
 * Please note, this router addition process is not thread-safe.
 */
class KeyUriRouter<URI>(
    private val logger: UriRouterLogger? = null,
    private val keyExtractor: (DeepLinkUri) -> String
) : UriRouter<URI>(logger) {

    private val keyToUriMap = mutableMapOf<String, MutableSet<Pair<DeepLinkUri, UriMatcher>>>()
    private val keyToEntryMap = mutableMapOf<String, MutableSet<Pair<DeepLinkEntry, UriMatcher>>>()

    private val handlerMap = mutableMapOf<DeepLinkUri, UriRouterHandler<URI>>()

    override fun clear() {
        keyToUriMap.clear()
    }

    override fun resolveEntry(route: String): Pair<DeepLinkEntry, EntryValue<URI>>? {
        val deepLinkUri = route.toDeepLinkUri()
        val key = keyExtractor(deepLinkUri)

        logger?.run {
            invoke("Key: $key")
            invoke("Entries map size: ${keyToEntryMap.size}")
            invoke("Uri map size: ${keyToUriMap.size}")
        }

        val result = getResultFromEntryMap(key, route)
        return if (result == null) {
            logger?.invoke("Get from uri map for key: $key")
            getResultFromUriMap(key, route).also {
                /**
                 * Because it's already available in [keyToEntryMap] remove the old data in [keyToUriMap]
                 */
                keyToUriMap.remove(key)
            }
        } else result
    }

    private fun getResultFromUriMap(
        key: String,
        route: String
    ): Pair<DeepLinkEntry, EntryValue<URI>>? {

        var entry: DeepLinkEntry? = null
        var matcher: UriMatcher? = null

        val uriList = keyToUriMap[key] ?: return null
        val actualKey = uriList.firstOrNull { pair ->
            entry = DeepLinkEntry.parse(pair.first)
            matcher = pair.second

            // Add parsed entry to entry map
            val sets = keyToEntryMap.getOrPut(key) { mutableSetOf() }
            sets.add(entry!! to matcher!!)

            matcher!!.match(entry!!, route)
        } ?: return null

        return createResult(actualKey.first, entry, matcher)
    }

    private fun getResultFromEntryMap(
        key: String,
        route: String
    ): Pair<DeepLinkEntry, EntryValue<URI>>? {
        val set = keyToEntryMap[key] ?: return null
        val actualKey = set.firstOrNull { (entry, matcher) ->
            matcher.match(entry, route)
        } ?: return null

        return createResult(actualKey.first.uri, actualKey.first, actualKey.second)
    }

    private fun createResult(
        keyUri: DeepLinkUri,
        entry: DeepLinkEntry?,
        matcher: UriMatcher?
    ): Pair<DeepLinkEntry, EntryValue<URI>> {
        val handler = handlerMap[keyUri] ?: error("Handler not available for $keyUri")

        return Pair(
            first = entry ?: error("Entry not available fro $keyUri"),
            second = EntryValue(
                handler,
                matcher ?: error("Matcher not available fro $keyUri")
            )
        )
    }

    /**
     * This is not thread-safe
     */
    override fun addEntry(
        vararg uri: String,
        matcher: UriMatcher,
        handler: UriRouterHandler<URI>
    ) {
        uri.forEach {
            val deepLinkUri = it.toDeepLinkUri()
            val key = keyExtractor(deepLinkUri)

            inputToEntryContainer(key, deepLinkUri, matcher)

            handlerMap[deepLinkUri] = handler
        }
    }

    private fun inputToEntryContainer(key: String, uri: DeepLinkUri, matcher: UriMatcher) {
        val entriesHolder = keyToUriMap.getOrPut(key) { mutableSetOf() }
        entriesHolder.add(uri to matcher)
    }
}