package nolambda.linkrouter

class SimpleUriRouter<RES>(logger: UriRouterLogger? = null) : UriRouter<RES>(logger) {

    override fun resolveEntry(route: String): Pair<DeepLinkEntry, EntryValue<RES>>? {
        return entries.asSequence().firstOrNull { entry ->
            val value = entry.value
            value.matcher.match(entry.key, route)
        }?.toPair()
    }
}