package nolambda.linkrouter

import nolambda.linkrouter.matcher.UriMatcher

/**
 * router that use "key" to group [DeepLinkUri] or [DeepLinkEntry] for faster
 * register and lookup
 *
 * Please note, this router addition process is not thread-safe.
 */
class KeyUriRouter<URI>(
    private val logger: UriRouterLogger? = null,
    private val keyExtractor: (String) -> String
) : UriRouter<URI>(logger) {

    /**
     * Key: From key extractor. Usually scheme + host + path
     * Value: Set of Pair of [DeepLinkEntry] and the matcher [UriMatcher]
     *
     * The result from here will be moved to [keyToEntryMap]
     * and the entry will be deleted after that
     */
    private val keyToUriMap = mutableMapOf<String, MutableSet<Pair<String, UriMatcher>>>()

    /**
     * Key: From key extractor. Usually scheme + host + path
     * Value: Set of Pair of [DeepLinkEntry] and the matcher [UriMatcher]
     *
     * If there's a value found in here, we don't need to search in [keyToUriMap]
     */
    private val keyToEntryMap =
        mutableMapOf<String, MutableSet<Triple<DeepLinkEntry, String, UriMatcher>>>()

    /**
     * Key: Registered route
     * Value: Registered lambda
     *
     * If we found registered route (key) from [keyToEntryMap] or [keyToUriMap]
     * we will find the registered lambda in here
     */
    private val handlerMap = mutableMapOf<String, UriRouterHandler<URI>>()

    override fun clear() {
        keyToUriMap.clear()
    }

    override fun resolveEntry(route: String): Pair<DeepLinkEntry, EntryValue<URI>>? {
        val key = keyExtractor(route)

        logger?.run {
            invoke("Key: $key")
            invoke("Entries map size: ${keyToEntryMap.size}")
            invoke("Uri map size: ${keyToUriMap.size}")
        }

        val result = getResultFromEntryMap(key, route)
        return if (result == null) {
            logger?.invoke("Get from uri map for key: $key")
            getResultFromUriMap(key, route)
        } else result
    }

    private fun getResultFromUriMap(
        key: String,
        route: String
    ): Pair<DeepLinkEntry, EntryValue<URI>>? {

        var entry: DeepLinkEntry? = null
        var matcher: UriMatcher? = null
        var pairOfUriAndMatcher: Pair<String, UriMatcher>? = null

        val uriList = keyToUriMap[key] ?: return null
        val actualKey = uriList.firstOrNull { pair ->

            val currentEntry = DeepLinkEntry.parse(pair.first)
            val currentMatcher = pair.second

            // Pass the data to outside variable, so we can use it for other case
            pairOfUriAndMatcher = pair
            entry = currentEntry
            matcher = currentMatcher

            // Add parsed entry to entry map
            val sets = keyToEntryMap.getOrPut(key) { mutableSetOf() }
            sets.add(Triple(currentEntry, pair.first, currentMatcher))

            currentMatcher.match(currentEntry, route)
        } ?: return null

        /**
         * Because it's already available in [keyToEntryMap] remove the old data in [keyToUriMap]
         */
        val sets = keyToUriMap[key]
        if (sets == null || sets.size <= 1) {
            keyToUriMap.remove(key)
        } else {
            sets.remove(pairOfUriAndMatcher)
        }

        return createResult(actualKey.first, entry, matcher)
    }

    private fun getResultFromEntryMap(
        key: String,
        route: String
    ): Pair<DeepLinkEntry, EntryValue<URI>>? {
        val set = keyToEntryMap[key] ?: return null
        val actualKey = set.firstOrNull { (entry, _, matcher) ->
            matcher.match(entry, route)
        } ?: return null

        return createResult(actualKey.second, actualKey.first, actualKey.third)
    }

    private fun createResult(
        registeredRoute: String,
        entry: DeepLinkEntry?,
        matcher: UriMatcher?
    ): Pair<DeepLinkEntry, EntryValue<URI>> {
        val handler = handlerMap[registeredRoute]
            ?: error("Handler not available for $registeredRoute")

        return Pair(
            first = entry ?: error("Entry not available fro $registeredRoute"),
            second = EntryValue(
                handler,
                matcher ?: error("Matcher not available fro $registeredRoute")
            )
        )
    }

    /**
     * This is not thread-safe
     */
    override fun addEntry(
        vararg uris: String,
        matcher: UriMatcher,
        handler: UriRouterHandler<URI>
    ) {
        uris.forEach { route ->
            val key = keyExtractor(route)
            inputToEntryContainer(key, route, matcher)
            handlerMap[route] = handler
        }
    }

    private fun inputToEntryContainer(key: String, registeredRoute: String, matcher: UriMatcher) {
        val entriesHolder = keyToUriMap.getOrPut(key) { mutableSetOf() }
        entriesHolder.add(registeredRoute to matcher)
    }
}