package nolambda.linkrouter.matcher

import nolambda.linkrouter.DeepLinkEntry

object DeepLinkEntryMatcher : UriMatcher {
    override fun match(entry: DeepLinkEntry, url: String): Boolean {
        return entry.matches(url)
    }
}