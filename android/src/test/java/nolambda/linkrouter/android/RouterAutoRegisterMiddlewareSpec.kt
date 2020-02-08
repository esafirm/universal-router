package nolambda.linkrouter.android

import android.content.Context
import io.kotlintest.specs.StringSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify

class RouterAutoRegisterMiddlewareSpec : StringSpec({
    val mockPlugin = mockk<RouterPlugin>(relaxed = true)
    val mockNameResolver = mockk<NameResolver>(relaxed = true)
    val routeAutoRegister = RouteAutoRegisterMiddleware(mockPlugin, mockNameResolver)

    val nameSlot = slot<String>()
    every {
        mockNameResolver.invoke(capture(nameSlot))
    } answers {
        "nolambda.linkrouter.android.${nameSlot.captured}"
    }

    "It should not register if the annotation is false" {
        every { mockPlugin.isUseAnnotationProcessor } returns false
        routeAutoRegister.onRouting(TestRoute(), null)

        verify(exactly = 0) {
            mockNameResolver.invoke(any())
        }
    }

    "It should resolve and invoke init" {
        every { mockPlugin.isUseAnnotationProcessor } returns true
        routeAutoRegister.onRouting(TestRoute(), null)

        verify(exactly = 1) {
            mockNameResolver.invoke(any())
        }
    }
})

class TestRoute : Route()

class TestRouteRouteInit(private val context: Context) : RouteInit {
    override fun onInit(appContext: Context) {

    }
}