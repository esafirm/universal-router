package nolambda.linkrouter

import io.kotest.core.spec.style.StringSpec
import java.util.Random
import kotlin.system.measureTimeMillis

class PerformanceTest : StringSpec({

    val size = 1000L
    val random = Random(size)

    val simpleRouter = object : UriRouter<Unit>() {}

    val generateEntry = {
        val domain = random.nextInt().toString()
        val simpleHttp = "http://${domain}.js/{kupon}/{customer_id}"
        val simpleHttps = "https://${domain}.js/{kupon}/{customer_id}"
        Pair(simpleHttp, simpleHttps)
    }

    val addEntry = { entry: Pair<String, String> ->
        simpleRouter.addEntry(entry.first, entry.second) { _, param ->
            print("Kupon ${param["kupon"]}")
        }
    }

    "warm up" {
        val time = measureTimeMillis {
            addEntry(generateEntry())
        }
        println("warm up took $time ms")
    }

    "add entries using map" {
        val entries = (0..size).map { generateEntry() }
        val time = measureTimeMillis {
            entries.forEach(addEntry)
        }
        println("sync takes $time ms to add $size entries")
    }

    "add entries using parallel stream" {
        val entries = (0..size).map { generateEntry() }
        val time = measureTimeMillis {
            entries.parallelStream().forEach(addEntry)
        }
        println("parallel takes $time ms to add $size entries")
    }

    "resolve time" {
        val entries = (0..size).map { generateEntry() }
        val resolveTime = measureTimeMillis {
            simpleRouter.resolve(entries.random().first)
        }
        println("resolve takes $resolveTime ms to resolve ${size * 2} entries")
    }

})

