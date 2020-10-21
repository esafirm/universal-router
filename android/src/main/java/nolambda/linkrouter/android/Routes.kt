package nolambda.linkrouter.android

import nolambda.linkrouter.DeepLinkUri

abstract class BaseRoute<P : Any>(
    vararg val routePaths: String
)

abstract class Route(
    vararg paths: String = emptyArray()
) : BaseRoute<Unit>(*paths)

abstract class RouteWithParam<P : Any>(
    vararg paths: String = emptyArray()
) : BaseRoute<P>(*paths) {
    fun isParamSameWithPath(): Boolean {
        return true
    }

    open fun mapUri(uri: DeepLinkUri, raw: Map<String, String>): P {
        throw IllegalStateException("mapUri should be override if you have paths")
    }
}