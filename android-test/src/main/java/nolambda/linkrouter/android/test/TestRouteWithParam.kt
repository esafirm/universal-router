package nolambda.linkrouter.android.test

import nolambda.linkrouter.android.RouteWithParam

open class TestRouteWithParam<Param : Any>(
    vararg paths: String = emptyArray()
) : RouteWithParam<Param>(*paths)