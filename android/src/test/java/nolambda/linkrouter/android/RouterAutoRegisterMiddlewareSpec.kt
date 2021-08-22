package nolambda.linkrouter.android

import android.content.Context
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import nolambda.linkrouter.android.autoregister.AutoRegister
import nolambda.linkrouter.android.autoregister.RouteAutoRegisterMiddleware
import nolambda.linkrouter.android.autoregister.RouteInit
import nolambda.linkrouter.android.test.TestRoute

@OptIn(AutoRegister::class)
class RouterAutoRegisterMiddlewareSpec : StringSpec({

    val testRouter = object : AbstractAppRouter<Any>() {}

    var isResolving = false
    val routeAutoRegister = RouteAutoRegisterMiddleware { name ->
        isResolving = true
        "nolambda.linkrouter.android.${name}"
    }

    "It should not register if the annotation is false" {
        RouterPlugin.isUseAnnotationProcessor = false

        val route = TestRoute()
        routeAutoRegister.onRouting(
            route,
            RouteParam(param = null, ActionInfo(route, currentRouter = testRouter))
        )

        isResolving shouldBe false
    }

    // TODO: This is ignored for now because we can't mock the Context
    "!It should resolve and invoke init" {
        RouterPlugin.isUseAnnotationProcessor = true

        val route = TestRoute()
        routeAutoRegister.onRouting(
            route,
            RouteParam(param = null, ActionInfo(route, currentRouter = testRouter))
        )

        isResolving shouldBe true
    }
})

@OptIn(AutoRegister::class)
class TestRouteRouteInit(private val context: Context) : RouteInit {
    override fun onInit(appContext: Context) {
        println("On TestRoute init!")
    }
}