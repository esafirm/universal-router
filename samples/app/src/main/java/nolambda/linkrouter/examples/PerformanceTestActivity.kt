package nolambda.linkrouter.examples

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_performance_test.*
import nolambda.linkrouter.DeepLinkEntry
import nolambda.linkrouter.android.AbstractAppRouter
import nolambda.linkrouter.android.KeyUriRouterFactory
import nolambda.linkrouter.android.Route
import nolambda.linkrouter.android.SimpleUriRouterFactory
import nolambda.linkrouter.android.registerstrategy.EagerRegisterStrategy
import nolambda.linkrouter.android.registerstrategy.LazyRegisterStrategy
import nolambda.linkrouter.examples.utils.isDebuggable
import kotlin.random.Random
import kotlin.system.measureTimeMillis

class PerformanceTestActivity : AppCompatActivity() {

    companion object {
        private const val ROUTES_SIZE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_performance_test)

        btn_test.setOnClickListener {
            txt_result.text = ""

            val isLazy = switch_lazy.isChecked
            val isNoUrl = switch_no_url.isChecked
            val isKeyUri = switch_key_uri.isChecked

            warmUp()

            doTest(
                isNoUrl = isNoUrl,
                isLazy = isLazy,
                isKeyUri = isKeyUri
            )
        }
    }

    private fun warmUp() {
        log("is debug: ${BuildConfig.DEBUG}")
        log("is debuggable: ${isDebuggable()}")

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
                object : Route("https://test.com/${Random.nextInt(5)}/$it") {}
            }
        }
        return (0 until ROUTES_SIZE).map {
            object : Route() {}
        }
    }

    private fun getTag(
        isNoUrl: Boolean,
        isLazy: Boolean,
        isKeyUri: Boolean
    ): String {
        return when {
            isKeyUri && isLazy -> "[KL]"
            isNoUrl && isLazy -> "[NL]"
            isNoUrl -> "[N]"
            isLazy -> "[L]"
            isKeyUri -> "[K]"
            else -> "[E]" // eager
        }
    }

    private fun doTest(
        isNoUrl: Boolean,
        isLazy: Boolean,
        isKeyUri: Boolean
    ) {
        val logger = ::log
        val routes = createRoutes(havePath = isNoUrl.not())
        val testRouter = object : AbstractAppRouter<Unit>(
            registerStrategy = when (isLazy) {
                true -> LazyRegisterStrategy()
                false -> EagerRegisterStrategy()
            },
            uriRouterFactory = when (isKeyUri) {
                true -> KeyUriRouterFactory(logger) {
                    val uri = Uri.parse(it)
                    "${uri.scheme}${uri.host}${uri.pathSegments.joinToString()}"
                }
                false -> SimpleUriRouterFactory(logger)
            }
        ) {}
        val size = routes.size

        val tag = getTag(isNoUrl, isLazy, isKeyUri)
        val t1 = measureWithPrint(
            log = { time -> "$tag registers took $time ms for $size entries" },
            block = {
                routes.forEach {
                    testRouter.addEntry(it)
                }
            }
        )

        val route = routes.random().routePaths.firstOrNull() ?: return
        val t2 = measureWithPrint(
            log = { time -> "$tag goTo took $time ms to resolve from $size entries" },
            block = { testRouter.goTo(route) }
        )

        val t3 = measureWithPrint(
            log = { time -> "$tag second goTo took $time ms to resolve from $size entries" },
            block = { testRouter.goTo(route) }
        )

        log("Total ${t1 + t2 + t3} ms")
    }

    private inline fun measureWithPrint(
        noinline log: (time: Long) -> String,
        block: () -> Unit
    ): Long {
        val time = measureTimeMillis(block)
        val logString = log(time)
        log(logString)
        return time
    }

    private fun AbstractAppRouter<Unit>.addEntry(route: Route) {
        register(route) {
            log("Triggered: ${it.info.uri}")
        }
    }

    private fun log(text: String) {
        txt_result.append(text)
        txt_result.append("\n")
        Log.d("Perf", text)
        println(text)
    }
}