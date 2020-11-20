package nolambda.linkrouter.android.test

import nolambda.linkrouter.android.AbstractAppRouter
import nolambda.linkrouter.android.Route
import nolambda.linkrouter.android.RouteParam
import nolambda.linkrouter.android.RouteWithParam

typealias RouterMatcher<P, E> = (RouteParam<P, E>) -> Boolean

fun AbstractAppRouter<*>.testHit(route: Route): Boolean {
    var isHit = false
    register(route) {
        isHit = true
    }
    push(route)
    return isHit
}

fun <P : Any, E> AbstractAppRouter<*>.testHit(
    route: RouteWithParam<P>,
    param: P,
    matcher: RouterMatcher<P, E> = RouterMatchers.hitMatcher(),
): Boolean {
    var testResult = false
    register(route) {
        testResult = matcher.invoke(it as RouteParam<P, E>)
    }
    push(route, param)
    return testResult
}

object RouterMatchers {
    fun <P, E> hitMatcher(): RouterMatcher<P, E> = { true }
}