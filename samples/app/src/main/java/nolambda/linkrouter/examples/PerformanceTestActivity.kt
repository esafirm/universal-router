package nolambda.linkrouter.examples

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_performance_test.*
import nolambda.linkrouter.android.AbstractAppRouter
import nolambda.linkrouter.android.Route
import kotlin.system.measureTimeMillis

class PerformanceTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_performance_test)

        val size = 1000
        btn_test.setOnClickListener { doTest(createRoutes(size, true)) }
        btn_test_no_url.setOnClickListener { doTest(createRoutes(size, false)) }
    }

    private fun createRoutes(size: Int, havePath: Boolean): List<Route> {
        if (havePath) {
            return (0 until size).map {
                object : Route("https://test.com/$it") {}
            }
        }
        return (0 until size).map {
            object : Route() {}
        }
    }

    private fun doTest(routes: List<Route>) {
        val testRouter = object : AbstractAppRouter<Unit>() {}
        val size = routes.size

        val addEntry = { route: Route ->
            testRouter.register(route) {
                println("Triggered: ${it.info.uri}")
            }
        }

        var time = measureTimeMillis {
            routes.forEach(addEntry)
        }
        println("sync registers took $time ms for $size entries")

        val route = routes.random().routePaths.firstOrNull() ?: return
        time = measureTimeMillis {
            testRouter.goTo(route)
        }
        println("goTo took $time ms to resolve from $size entries")
    }
}