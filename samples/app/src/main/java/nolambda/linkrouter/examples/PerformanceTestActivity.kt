package nolambda.linkrouter.examples

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_performance_test.*
import nolambda.linkrouter.DeepLinkEntry
import nolambda.linkrouter.android.AbstractAppRouter
import nolambda.linkrouter.android.Route
import nolambda.linkrouter.android.registerstrategy.EagerRegisterStrategy
import nolambda.linkrouter.android.registerstrategy.LazyRegisterStrategy
import nolambda.linkrouter.examples.utils.isDebuggable
import kotlin.system.measureTimeMillis

class PerformanceTestActivity : AppCompatActivity() {

    companion object {
        private const val ROUTES_SIZE = 1_000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_performance_test)

        btn_test.setOnClickListener {
            val isLazy = switch_lazy.isChecked
            val isNoUrl = switch_no_url.isChecked

            warmUp()

            doTest(
                isNoUrl = isNoUrl,
                isLazy = isLazy
            )
        }
    }

    private fun warmUp() {
        Log.d("Perf", "is debug: ${BuildConfig.DEBUG}")
        Log.d("Perf", "is debuggable: ${isDebuggable()}")

        measureWithPrint(
            log = { "warm up took $it ms" },
            block = {
                // This have initialization cost
                DeepLinkEntry.parse("https://test.com/go")
            }
        )
    }

    private fun createRoutes(havePath: Boolean): List<Route> {
        if (havePath) {
            return (0 until ROUTES_SIZE).map {
                object : Route("https://test.com/$it") {}
            }
        }
        return (0 until ROUTES_SIZE).map {
            object : Route() {}
        }
    }

    private fun doTest(
        isNoUrl: Boolean,
        isLazy: Boolean
    ) {
        val routes = createRoutes(havePath = isNoUrl.not())
        val testRouter = object : AbstractAppRouter<Unit>(
            registerStrategy = when (isLazy) {
                true -> LazyRegisterStrategy()
                false -> EagerRegisterStrategy()
            }
        ) {}
        val size = routes.size

        val tag = when {
            isNoUrl && isLazy -> "[NL]"
            isNoUrl -> "[N]"
            isLazy -> "[L]"
            else -> "[E]"
        }

        measureWithPrint(
            log = { time -> "$tag sync registers took $time ms for $size entries" },
            block = {
                routes.forEach {
                    testRouter.addEntry(it)
                }
            }
        )

        val route = routes.random().routePaths.firstOrNull() ?: return
        measureWithPrint(
            log = { time -> "$tag goTo took $time ms to resolve from $size entries" },
            block = { testRouter.goTo(route) }
        )
    }

    private inline fun measureWithPrint(
        noinline log: (time: Long) -> String,
        block: () -> Unit
    ) {
        val time = measureTimeMillis(block)
        val logString = log(time)
        println(logString)
        Log.d("Perf", logString)
    }

    private fun AbstractAppRouter<Unit>.addEntry(route: Route) {
        register(route) {
            println("Triggered: ${it.info.uri}")
        }
    }
}