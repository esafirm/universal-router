package nolambda.linkrouter.android

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import nolambda.linkrouter.android.AndroidRoutes.HomeRoute
import nolambda.linkrouter.android.AndroidRoutes.ProductDetailRoute
import nolambda.linkrouter.android.AndroidRoutes.UserRouter
import nolambda.linkrouter.optString

object AndroidRoutes {
    object HomeRoute : Route()
    object ProductDetailRoute : Route("nolambda://detail/{product_id}")
    object UserRouter : RouteWithParam<UserRouter.UserParam>(
        path = "nolambda://user/{user_id}",
        paramMapper = {
            UserParam(it.optString("user_id"))
        }
    ) {
        data class UserParam(val userId: String)
    }
}

class RouterSpec : StringSpec({

    "routing should be working" {
        var homeState = false
        HomeRoute.register {
            homeState = true
        }

        Router.push(HomeRoute)

        homeState shouldBe true
    }

    "routing with uri should be working" {
        var productId = 0
        ProductDetailRoute.register {
            productId = it.rawParam.getOrDefault("product_id", "0").toInt()
        }

        Router.goTo("nolambda://detail/1")

        productId shouldBe 1
    }

    "routing with parameter should be working" {
        var userId = 0
        UserRouter.register {
            userId = it.param?.userId?.toInt() ?: 0
        }

        Router.push(UserRouter, UserRouter.UserParam("1"))

        userId shouldBe 1
    }

    "routing with uri in route with param should be working" {
        var userId = 0
        Router.cleanRouter()
        UserRouter.register {
            userId = it.rawParam["user_id"]?.toInt() ?: 0
        }

        Router.goTo("nolambda://user/1")

        userId shouldBe 1
    }
})