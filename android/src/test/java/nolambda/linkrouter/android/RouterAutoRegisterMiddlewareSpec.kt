package nolambda.linkrouter.android

import android.content.Context
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import nolambda.linkrouter.android.autoregister.AutoRegister
import nolambda.linkrouter.android.autoregister.NameResolver
import nolambda.linkrouter.android.autoregister.RouteAutoRegisterMiddleware
import nolambda.linkrouter.android.autoregister.RouteInit

@OptIn(AutoRegister::class)
class RouterAutoRegisterMiddlewareSpec : StringSpec({
    val mockPlugin = mockk<RouterPlugin>(relaxed = true)
    val mockNameResolver = mockk<NameResolver>(relaxed = true)
    val routeAutoRegister = RouteAutoRegisterMiddleware(mockPlugin, mockNameResolver)
    val mockRouter = mockk<AbstractAppRouter<*>>(relaxed = true)

    val nameSlot = slot<String>()
    every {
        mockNameResolver.invoke(capture(nameSlot))
    } answers {
        "nolambda.linkrouter.android.${nameSlot.captured}"
    }

    "It should not register if the annotation is false" {
        every { mockPlugin.isUseAnnotationProcessor } returns false
        routeAutoRegister.onRouting(
            TestRoute(),
            RouteParam(param = null, ActionInfo(false, mockRouter))
        )

        verify(exactly = 0) {
            mockNameResolver.invoke(any())
        }
    }

    "It should resolve and invoke init" {
        every { mockPlugin.isUseAnnotationProcessor } returns true
        routeAutoRegister.onRouting(
            TestRoute(),
            RouteParam(param = null, ActionInfo(false, mockRouter))
        )

        verify(exactly = 1) {
            mockNameResolver.invoke(any())
        }
    }
})

class TestRoute : Route()

@OptIn(AutoRegister::class)
class TestRouteRouteInit(private val context: Context) : RouteInit {
    override fun onInit(appContext: Context) {

    }
}