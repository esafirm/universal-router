package nolambda.linkrouter.matcher

import nolambda.linkrouter.DeepLinkEntry

interface UriMatcher {
    fun match(entry: DeepLinkEntry, url: String): Boolean
}