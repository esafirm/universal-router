package nolambda.linkrouter.android

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import nolambda.linkrouter.DeepLinkUri
import nolambda.linkrouter.android.AndroidRoutes.HomeRoute
import nolambda.linkrouter.android.AndroidRoutes.ProductDetailRoute
import nolambda.linkrouter.android.AndroidRoutes.UserRoute
import nolambda.linkrouter.error.RouteNotFoundException

object AndroidRoutes {
    object HomeRoute : Route()
    object ProductDetailRoute : Route()
    object UserRoute : RouteWithParam<UserRoute.UserParam>(
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


class RouterSpec : StringSpec({

    val testRouter = object : AbstractAppRouter<Unit>() {}

    "routing should be working" {
        testRouter.register(HomeRoute) { true }
        val result = testRouter.push(HomeRoute)

        result.isHandled shouldBe true
        result.getResultOrError<Boolean>() shouldBe true
    }

    "routing with parameter should be working" {
        testRouter.register(UserRoute) {
            it.param?.userId?.toInt() ?: 0
        }

        val result = testRouter.push(UserRoute, UserRoute.UserParam("1"))
        val userId: Int = result.getResultOrError()

        userId shouldBe 1
    }

    "routing with different return type should be working" {
        testRouter.cleanRouter()
        testRouter.register(UserRoute) {
            val userId = it.param?.userId ?: ""
            if (userId == "1") {
                true
            } else {
                1
            }
        }

        val resultOne = testRouter.push(UserRoute, UserRoute.UserParam("1")).getResultOrError<Boolean>()
        val resultTwo = testRouter.push(UserRoute, UserRoute.UserParam("2")).getResultOrError<Int>()

        resultOne shouldBe true
        resultTwo shouldBe 1
    }

    "routing with uri in route with param should be working" {
        testRouter.cleanRouter()

        testRouter.register(UserRoute) {
            it.param?.userId?.toInt() ?: 0
        }

        val result = testRouter.goTo("nolambda://user/1")
        val userId: Int = result.getResultOrError()

        result.isHandled shouldBe true
        userId shouldBe 1
    }

    "routing with uri containing query should be working" {
        testRouter.cleanRouter()

        testRouter.register(UserRoute) {
            it.param?.userId?.toInt() ?: 0
        }

        val result = testRouter.goTo("app://user?user_id=1")
        val userId = result.getResultOrError<Int>()

        userId shouldBe 1
    }

    "routing another uri in route should be working" {
        testRouter.cleanRouter()

        testRouter.register(UserRoute) {
            it.param?.userId?.toInt() ?: 0
        }

        val result = testRouter.goTo("https://nolambda.stream/1")
        val userId: Int = result.getResultOrError()

        userId shouldBe 1
    }

    "routing non exist path should not trigger exception" {
        testRouter.cleanRouter()

        val result = testRouter.goTo("testing://aaa")
        result.isHandled shouldBe false
    }

    "param mapper should be working" {
        testRouter.cleanRouter()

        testRouter.register(UserRoute) {
            it.param!!.userId.toInt()
        }

        val result = testRouter.goTo("nolambda://user/1")
        val userId: Int = result.getResultOrError()

        userId shouldBe 1
    }

    "processor invoked when the type is right" {
        testRouter.cleanRouter()

        open class Parent
        class Child : Parent()

        var stringInvoked = false
        var intInvoked = false
        var childInvoked = false

        val returnedString = "This is home route"
        val returnedChild = Child()

        testRouter.register(HomeRoute) { returnedString }
        testRouter.register(ProductDetailRoute) { returnedChild }

        testRouter.addProcessor<String> { it, _ ->
            stringInvoked = true
            it shouldBe returnedString
        }
        testRouter.addProcessor<Int> { _, _ ->
            intInvoked = true
        }
        testRouter.addProcessor<Child> { it, _ ->
            childInvoked = true
            it shouldBe returnedChild
        }

        testRouter.push(HomeRoute)
        testRouter.push(ProductDetailRoute)

        stringInvoked shouldBe true
        intInvoked shouldBe false
        childInvoked shouldBe true
    }

    "it should throw exception if route is not registered" {
        testRouter.cleanRouter()
        shouldThrow<RouteNotFoundException> {
            testRouter.push(HomeRoute)
        }
    }

    "it should throw exception if trying to use uri without defining mapUri" {
        testRouter.cleanRouter()

        testRouter.register(AndroidRoutes.PathNoMap) { "" }
        shouldThrow<IllegalStateException> {
            testRouter.goTo("app://appkeren")
        }
    }

    "it should trigger error handler from plugin" {
        testRouter.cleanRouter()

        var isInvoked = false
        RouterPlugin.errorHandler = {
            isInvoked = true
        }
        testRouter.push(HomeRoute)

        isInvoked shouldBe true
    }

    "it should resolve to expected route" {
        testRouter.cleanRouter()
        testRouter.register(UserRoute) { }

        val result = testRouter.resolveUri("nolambda://user/1")
        result?.route shouldBe UserRoute

        val emptyResult = testRouter.resolveUri("app://appkeren")
        emptyResult shouldBe null
    }
})
