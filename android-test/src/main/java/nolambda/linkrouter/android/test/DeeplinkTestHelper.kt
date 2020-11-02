package nolambda.linkrouter.android.test

import nolambda.linkrouter.android.AbstractAppRouter
import nolambda.linkrouter.android.BaseRoute
import nolambda.linkrouter.android.RouteParam

data class DeepLinkAssertionData<P : Any, E>(
    val routeParam: RouteParam<P, E>?,
    val route: BaseRoute<P>,
    val currentPath: String
)

typealias DeepLinkAssertion<P, E> = (DeepLinkAssertionData<P, E>) -> Unit
typealias DeepLinkTestPair<P, E> = Pair<String, DeepLinkAssertion<P, E>>
typealias DeepLinkRoute<P, E> = Pair<BaseRoute<P>, List<DeepLinkTestPair<P, E>>>

fun <P : Any, E> AbstractAppRouter<E>.testDeepLink(
    deepLinkData: List<DeepLinkRoute<P, E>>,
    clean: Boolean = true
) {
    val router = this
    if (clean) {
        router.cleanRouter()
    }

    deepLinkData.forEach { (route, testData) ->
        var lastRouteParam: RouteParam<P, E>?
        router.register(route) {
            lastRouteParam = it
        }
        testData.forEach { (path, assertion) ->
            lastRouteParam = null
            router.goTo(path)
            assertion(DeepLinkAssertionData(
                routeParam = lastRouteParam,
                route = route,
                currentPath = path
            ))
        }
    }
}

object DeepLinkAssertions {
    fun <P : Any, E> shouldValid(isValid: Boolean = true): DeepLinkAssertion<P, E> = { p ->
        check(p.routeParam != null == isValid) { "DeepLink is not handled: ${p.currentPath}" }
    }
}