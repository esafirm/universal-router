package nolambda.linkrouter.android

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import nolambda.linkrouter.DeepLinkUri
import nolambda.linkrouter.android.AndroidRoutes.HomeRoute
import nolambda.linkrouter.android.AndroidRoutes.ProductDetailRoute
import nolambda.linkrouter.android.AndroidRoutes.UserRouter

object AndroidRoutes {
    object HomeRoute : Route()
    object ProductDetailRoute : Route()
    object UserRouter : RouteWithParam<UserRouter.UserParam>(
        "nolambda://user/{user_id}", "https://nolambda.stream/{user_id}", "app://user"
    ) {
        data class UserParam(val userId: String)

        override fun mapUri(uri: DeepLinkUri, raw: Map<String, String>): UserParam {
            val userId = raw["user_id"] ?: uri.queryParameter("user_id") ?: ""
            return UserParam(userId)
        }
    }

    object PathNoMap : RouteWithParam<String>("app://appkeren")
}

internal object TestRouter : AbstractAppRouter<Unit>()

internal fun <P : Any, R> BaseRoute<P>.register(handler: RouteHandler<P, R, Unit>) {
    TestRouter.register(this, handler)
}

class RouterSpec : StringSpec({

    "routing should be working" {
        var homeState = false
        HomeRoute.register {
            homeState = true
        }

        TestRouter.push(HomeRoute)

        homeState shouldBe true
    }

    "routing with parameter should be working" {
        var userId = 0
        UserRouter.register {
            userId = it.param?.userId?.toInt() ?: 0
        }

        TestRouter.push(UserRouter, UserRouter.UserParam("1"))

        userId shouldBe 1
    }

    "routing with uri in route with param should be working" {
        TestRouter.cleanRouter()

        var userId = 0
        UserRouter.register {
            userId = it.param?.userId?.toInt() ?: 0
        }

        val result = TestRouter.goTo("nolambda://user/1")

        result shouldBe true
        userId shouldBe 1
    }

    "routing with uri containing query should be working" {
        TestRouter.cleanRouter()

        var userId = 0
        UserRouter.register {
            userId = it.param?.userId?.toInt() ?: 0
        }

        TestRouter.goTo("app://user?user_id=1")

        userId shouldBe 1
    }

    "routing another uri in route should be working" {
        TestRouter.cleanRouter()

        var userId = 0
        UserRouter.register {
            userId = it.param?.userId?.toInt() ?: 0
        }

        TestRouter.goTo("https://nolambda.stream/1")

        userId shouldBe 1
    }

    "routing non exist path should not trigger execption" {
        TestRouter.cleanRouter()

        val result = TestRouter.goTo("testing://aaa")
        result shouldBe false
    }

    "param mapper should be working" {
        TestRouter.cleanRouter()

        var userId = 0
        UserRouter.register {
            userId = it.param!!.userId.toInt()
        }

        TestRouter.goTo("nolambda://user/1")

        userId shouldBe 1
    }

    "processor invoked when the type is right" {
        TestRouter.cleanRouter()

        open class Parent
        class Child : Parent()

        var stringInvoked = false
        var intInvoked = false
        var childInvoked = false

        val returnedString = "This is home route"
        val returnedChild = Child()

        HomeRoute.register { returnedString }
        ProductDetailRoute.register { returnedChild }

        TestRouter.addProcessor<String> { it, _ ->
            stringInvoked = true
            it shouldBe returnedString
        }
        TestRouter.addProcessor<Int> { _, _ ->
            intInvoked = true
        }
        TestRouter.addProcessor<Child> { it, _ ->
            childInvoked = true
            it shouldBe returnedChild
        }

        TestRouter.push(HomeRoute)
        TestRouter.push(ProductDetailRoute)

        stringInvoked shouldBe true
        intInvoked shouldBe false
        childInvoked shouldBe true
    }

    "it should throw exception if route is not registered" {
        TestRouter.cleanRouter()
        shouldThrow<IllegalStateException> {
            TestRouter.push(HomeRoute)
        }
    }

    "it should throw exception if trying to push without param" {
        TestRouter.cleanRouter()

        TestRouter.register(UserRouter) { "" }
        shouldThrow<IllegalArgumentException> {
            TestRouter.push(UserRouter, UserRouter.UserParam("1"))
        }
    }

    "it should throw exception if trying to use uri without defining mapUri" {
        TestRouter.cleanRouter()

        TestRouter.register(AndroidRoutes.PathNoMap) { "" }
        shouldThrow<IllegalStateException> {
            TestRouter.goTo("app://appkeren")
        }
    }

    "it should trigger error handler from plugin" {
        TestRouter.cleanRouter()

        var isInvoked = false
        RouterPlugin.errorHandler = {
            isInvoked = true
        }
        TestRouter.push(HomeRoute)

        isInvoked shouldBe true
    }
})
