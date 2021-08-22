package nolambda.linkrouter.android

import nolambda.linkrouter.DeepLinkUri
import nolambda.linkrouter.matcher.DeepLinkEntryMatcher
import nolambda.linkrouter.matcher.UriMatcher
import java.io.Serializable

abstract class BaseRoute<P : Any>(
    vararg val routePaths: String
) : Serializable {
    open fun pathMatcher(): UriMatcher = DeepLinkEntryMatcher
}

abstract class Route(
    vararg paths: String = emptyArray()
) : BaseRoute<Unit>(*paths)

abstract class RouteWithParam<P : Any>(
    vararg paths: String = emptyArray()
) : BaseRoute<P>(*paths) {

    open fun mapUri(uri: DeepLinkUri, raw: Map<String, String>): P {
        throw IllegalStateException("mapUri should be override if you have paths")
    }
}