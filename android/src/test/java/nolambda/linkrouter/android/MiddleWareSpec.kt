package nolambda.linkrouter.android

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class MiddleWareSpec : StringSpec({
    val firstRoute = object : Route() {}
    val secondRoute = object : Route() {}

    var shouldReroute = false
    val rerouteMiddleware = object : Middleware<Unit> {
        override fun onRouting(route: BaseRoute<*>, routeParam: RouteParam<*, Unit>): BaseRoute<*> {
            return if (shouldReroute) {
                firstRoute
            } else {
                route
            }
        }
    }

    val testRouter = object : AbstractAppRouter<Unit>(rerouteMiddleware) {}

    "Middleware should return original route" {
        testRouter.testHit(firstRoute) shouldBe true
        testRouter.testHit(secondRoute) shouldBe true
    }

    "middleware should reroute" {
        shouldReroute = true
        testRouter.testHit(firstRoute) shouldBe true
        testRouter.testHit(secondRoute) shouldBe false
    }

})