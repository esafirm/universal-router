package nolambda.linkrouter.android

abstract class BaseRoute<P>(
    vararg val routePaths: String
) {
    fun <R> register(handler: RouteHandler<P, R>) {
        Router.register(this, handler)
    }
}

abstract class Route(
    vararg paths: String = emptyArray()
) : BaseRoute<Unit>(*paths)

abstract class RouteWithParam<P>(
    vararg paths: String = emptyArray()
) : BaseRoute<P>(*paths) {
    fun isParamSameWithPath(): Boolean {
        return true
    }

    abstract fun mapParameter(raw: Map<String, String>): P
}