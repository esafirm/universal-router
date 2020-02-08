package nolambda.linkrouter.android

interface Middleware {
    fun onRouting(route: BaseRoute<*>, param: Any?)
}