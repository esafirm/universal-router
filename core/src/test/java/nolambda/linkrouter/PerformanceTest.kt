package nolambda.linkrouter

import io.kotest.core.spec.style.StringSpec
import java.util.Random
import kotlin.system.measureTimeMillis

class PerformanceTest : StringSpec({

    val random = Random(10_000)

    val simpleRouter = object : UriRouter<Unit>() {}

    val generateEntry = {
        val domain = random.nextInt().toString()
        val simpleHttp = "http://${domain}.js/{kupon}/{customer_id}"
        val simpleHttps = "https://${domain}.js/{kupon}/{customer_id}"

        simpleRouter.addEntry(simpleHttp, simpleHttps) { _, param ->
            print("Kupon ${param["kupon"]}")
        }

        simpleHttp
    }


    val entries = mutableListOf<String>()
    val time = measureTimeMillis {
        (0..10_000).forEach { _ ->
            entries.add(generateEntry())
        }
    }
    println("It takes $time ms to add")

    val resolveTime = measureTimeMillis {
        simpleRouter.resolve(entries.random())
    }
    println("It takes $resolveTime ms to resolve")
})

