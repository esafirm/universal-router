package nolambda.linkrouter.android

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import nolambda.linkrouter.android.AndroidRoutes.HomeRoute
import nolambda.linkrouter.android.AndroidRoutes.ProductDetailRoute
import nolambda.linkrouter.android.AndroidRoutes.UserRouter
import nolambda.linkrouter.optString

object AndroidRoutes {
    object HomeRoute : Route()
    object ProductDetailRoute : Route()
    object UserRouter : RouteWithParam<UserRouter.UserParam>(
        "nolambda://user/{user_id}", "https://nolambda.stream/{user_id}"
    ) {
        override fun mapParameter(raw: Map<String, String>): UserParam {
            return UserParam(raw.optString("user_id"))
        }

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

    "routing with parameter should be working" {
        var userId = 0
        UserRouter.register {
            userId = it.param?.userId?.toInt() ?: 0
        }

        Router.push(UserRouter, UserRouter.UserParam("1"))

        userId shouldBe 1
    }

    "routing with uri in route with param should be working" {
        Router.cleanRouter()

        var userId = 0
        UserRouter.register {
            userId = it.param?.userId?.toInt() ?: 0
        }

        Router.goTo("nolambda://user/1")

        userId shouldBe 1
    }

    "routing another uri in route should be working" {
        Router.cleanRouter()

        var userId = 0
        UserRouter.register {
            userId = it.param?.userId?.toInt() ?: 0
        }

        Router.goTo("https://nolambda.stream/1")

        userId shouldBe 1
    }

    "param mapper should be working" {
        Router.cleanRouter()

        var userId = 0
        UserRouter.register {
            userId = it.param!!.userId.toInt()
        }

        Router.goTo("nolambda://user/1")

        userId shouldBe 1
    }

    "processor invoked when the type is right" {
        Router.cleanRouter()

        open class Parent
        class Child : Parent()

        var stringInvoked = false
        var intInvoked = false
        var childInvoked = false

        val returnedString = "This is home route"
        val returnedChild = Child()

        HomeRoute.register { returnedString }
        ProductDetailRoute.register { returnedChild }

        Router.addProcessor<String> {
            stringInvoked = true
            it shouldBe returnedString
        }
        Router.addProcessor<Int> { intInvoked = true }
        Router.addProcessor<Child> {
            childInvoked = true
            it shouldBe returnedChild
        }

        Router.push(HomeRoute)
        Router.push(ProductDetailRoute)

        stringInvoked shouldBe true
        intInvoked shouldBe false
        childInvoked shouldBe true
    }
})