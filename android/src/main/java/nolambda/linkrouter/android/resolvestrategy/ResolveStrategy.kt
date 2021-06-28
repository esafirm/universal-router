package nolambda.linkrouter.android.resolvestrategy

import nolambda.linkrouter.android.UriResult

interface ResolveStrategy {
    fun resolve(uri: String): UriResult?
}