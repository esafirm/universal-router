package nolambda.linkrouter.android

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import nolambda.linkrouter.DeepLinkEntry
import nolambda.linkrouter.android.registerstrategy.LazyRegisterStrategy
import kotlin.system.measureTimeMillis

class AndroidPerformanceTest : StringSpec({
    val eagerRouter = object : AbstractAppRouter<Unit>() {}
    val lazyRouter = object : AbstractAppRouter<Unit>(registerStrategy = LazyRegisterStrategy()) {}

    val size = 10_000L

    val routes = (0 until size).map {
        object : Route("https://test.com/$it") {}
    }

    val register = { router: AbstractAppRouter<Unit> ->
        val time = measureTimeMillis {
            routes.forEach { route ->
                router.register(route) {
                    val result = route.routePaths.first()
                    println("Triggered: ${it.info.uri}")
                    result
                }
            }
        }
        println("registers took $time ms for $size entries")
    }

    val goTo = { router: AbstractAppRouter<Unit> ->
        val time = measureTimeMillis {
            val route = routes.random().routePaths.first()
            val result = router.goTo(route)

            println("Go to => $route")

            // Assert
            result.isHandled shouldBe true
            result.getResultOrNull<String>() shouldBe route
        }
        println("goTo took $time ms to resolve from $size entries")
    }

    "warm up" {
        DeepLinkEntry.parse(routes.first().routePaths.first())
    }

    "eager register speed" {
        register(eagerRouter)
    }

    "eager go to speed" {
        goTo(eagerRouter)
    }

    "lazy strategy register" {
        register(lazyRouter)
    }

    "lazy go to speed" {
        goTo(lazyRouter)
    }
})

