package nolambda.linkrouter.android.test

import nolambda.linkrouter.android.AbstractAppRouter
import nolambda.linkrouter.android.Route
import nolambda.linkrouter.android.RouteWithParam

fun AbstractAppRouter<*>.testHit(route: Route): Boolean {
    var isHit = false
    register(route) {
        isHit = true
    }
    push(route)
    return isHit
}

fun <P : Any> AbstractAppRouter<*>.testHit(route: RouteWithParam<P>, param: P): Boolean {
    var isHit = false
    register(route) {
        isHit = true
    }
    push(route, param)
    return isHit
}