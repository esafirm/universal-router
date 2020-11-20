package nolambda.linkrouter.android.test

import nolambda.linkrouter.android.AbstractAppRouter
import nolambda.linkrouter.android.Route
import nolambda.linkrouter.android.RouteParam
import nolambda.linkrouter.android.RouteWithParam

typealias RouterMatcher<P, E> = (RouteParam<P, E>) -> Boolean

@Suppress("UNCHECKED_CAST")
fun <E> AbstractAppRouter<E>.testHit(
    route: Route,
    matcher: RouterMatcher<Unit, E> = RouterMatchers.hitMatcher(),
): Boolean {
    var testResult = false
    register(route) {
        testResult = matcher.invoke(it)
    }
    push(route)
    return testResult
}

@Suppress("UNCHECKED_CAST")
fun <P : Any, E> AbstractAppRouter<E>.testHit(
    route: RouteWithParam<P>,
    param: P,
    matcher: RouterMatcher<P, E> = RouterMatchers.hitMatcher(),
): Boolean {
    var testResult = false
    register(route) {
        testResult = matcher.invoke(it)
    }
    push(route, param)
    return testResult
}

object RouterMatchers {
    fun <P, E> hitMatcher(): RouterMatcher<P, E> = { true }
}