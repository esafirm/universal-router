package nolambda.linkrouter.android

abstract class BaseRoute<P>(
    val path: String
) {
    fun register(handler: RouteHandler<P>) {
        Router.register(this, handler)
    }
}

abstract class Route(path: String = "") : BaseRoute<Unit>(path)

typealias ParamMapper<T> = (Map<String, String>) -> T

abstract class RouteWithParam<P>(
    path: String = "",
    val paramMapper: ParamMapper<P>? = null
) : BaseRoute<P>(path) {
    fun isParamSameWithPath(): Boolean {
        return true
    }
}