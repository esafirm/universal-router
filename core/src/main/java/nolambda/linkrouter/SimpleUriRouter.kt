package nolambda.linkrouter

class SimpleUriRouter<RES>(logger: UriRouterLogger? = null) : UriRouter<RES>(logger) {

    override fun resolveEntry(route: String): Pair<DeepLinkEntry, EntryValue<RES>>? {
        return entries.toList().firstOrNull {
            it.second.matcher.match(it.first, route)
        }
    }
}