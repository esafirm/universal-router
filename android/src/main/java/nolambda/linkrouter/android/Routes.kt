package nolambda.linkrouter.android

abstract class BaseRoute<P>(
    vararg val routePaths: String
) {
    fun register(handler: RouteHandler<P>) {
        Router.register(this, handler)
    }
}

abstract class Route(
    vararg paths: String = emptyArray()
) : BaseRoute<Unit>(*paths)

typealias ParamMapper<T> = (Map<String, String>) -> T

abstract class RouteWithParam<P>(
    val paths: Array<String> = emptyArray(),
    val paramMapper: ParamMapper<P>? = null
) : BaseRoute<P>(*paths) {
    fun isParamSameWithPath(): Boolean {
        return true
    }
}