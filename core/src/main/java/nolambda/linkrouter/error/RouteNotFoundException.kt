package nolambda.linkrouter.error

class RouteNotFoundException(val route: Any) : Exception("No entry for parameter $route")