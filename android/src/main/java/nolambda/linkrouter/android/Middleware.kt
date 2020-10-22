package nolambda.linkrouter.android

interface Middleware<Extra> {
    fun onRouting(route: BaseRoute<*>, routeParam: RouteParam<*, Extra>)
}