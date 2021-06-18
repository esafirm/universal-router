package nolambda.linkrouter.android

import io.kotest.core.spec.style.StringSpec
import kotlin.system.measureTimeMillis

class AndroidPerformanceTest : StringSpec({
    val testRouter = object : AbstractAppRouter<Unit>() {}
    val size = 1_000L

    val routes = (0 until size).map {
        object : Route("https://test.com/$it") {}
    }

    "register speed" {
        val time = measureTimeMillis {
            routes.forEach { route ->
                testRouter.register(route) {
                    println("Triggered: ${it.info.uri}")
                }
            }
        }
        println("registers took $time ms for $size entries")
    }

    "go to speed" {
        val time = measureTimeMillis {
            testRouter.goTo(routes.random().routePaths.first())
        }
        println("goTo took $time ms to resolve from $size entries")
    }
})

