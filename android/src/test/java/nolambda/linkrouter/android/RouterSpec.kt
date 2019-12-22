package nolambda.linkrouter.android

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import nolambda.linkrouter.android.AndroidRoutes as R

object AndroidRoutes {
    object HomeRoute : Route()
    object ProductDetailRoute : Route("nolambda://detail/{product_id}")
    object UserRouter : RouteWithParam<UserRouter.UserParam>("nolambda://user/{user_id}") {
        data class UserParam(val userId: String)
    }
}

class RouterSpec : StringSpec({

    "routing should be working" {
        var homeState = false
        R.HomeRoute.register {
            homeState = true
        }

        Router.push(R.HomeRoute)

        homeState shouldBe true
    }

    "routing with parameter should be working" {
        var userId = 0
        R.UserRouter.register {
            userId = it.param?.userId?.toInt() ?: 0
        }

        Router.push(R.UserRouter, R.UserRouter.UserParam("1"))

        userId shouldBe 1
    }

    "routing with uri in route with param should be working" {
        var userId = 0
        Router.cleanRouter()
        R.UserRouter.register {
            userId = it.mapParam["user_id"]?.toInt() ?: 0
        }

        Router.goTo("nolambda://user/1")

        userId shouldBe 1
    }
})