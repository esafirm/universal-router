package nolambda.linkrouter.android

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import nolambda.linkrouter.android.middlewares.MiddleWareResult
import nolambda.linkrouter.android.middlewares.Middleware
import nolambda.linkrouter.android.test.testHit

class MiddleWareSpec : StringSpec({
    val firstRoute = object : Route() {}
    val secondRoute = object : Route() {}
    val thirdRoute = object : RouteWithParam<String>() {}

    var shouldReroute = false
    val rerouteMiddleware = object : Middleware<Unit> {
        override fun onRouting(route: BaseRoute<*>, routeParam: RouteParam<*, Unit>): MiddleWareResult<Unit> {
            if (route == thirdRoute) {
                return MiddleWareResult(route, routeParam.copyWithParam(""))
            }

            val finalRoute = if (shouldReroute) {
                firstRoute
            } else {
                route
            }
            return MiddleWareResult(finalRoute, routeParam)
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

    "Middleware should replace the param" {
        var isHit = false
        testRouter.register(thirdRoute) {
            isHit = true
            it.param shouldBe ""
        }
        testRouter.push(thirdRoute, "ABC")
        isHit shouldBe true
    }

})