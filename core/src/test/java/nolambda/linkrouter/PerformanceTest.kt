package nolambda.linkrouter

import io.kotest.core.spec.style.StringSpec
import java.util.Random
import kotlin.system.measureTimeMillis

class PerformanceTest : StringSpec({

    val size = 500L
    val random = Random(size)

    val logger = { log: String -> println(log) }

    val simpleRouter = object : UriRouter<Unit>(logger) {}
    val parallelRouter = object : UriRouter<Unit>(logger, parallelResolve = true) {}

    val generateEntry = {
        val domain = random.nextInt().toString()
        val simpleHttp = "http://${domain}.js/{kupon}/{customer_id}"
        val simpleHttps = "https://${domain}.js/{kupon}/{customer_id}"
        Pair(simpleHttp, simpleHttps)
    }

    val addEntry = { router: UriRouter<Unit>, entry: Pair<String, String> ->
        router.addEntry(entry.first, entry.second) { _, _ ->
            print("Resolved!")
        }
    }

    val entries = (0 until size).map { generateEntry() }
    val testEntry = entries[entries.size / 2]

    "warm up" {
        val time = measureTimeMillis {
            addEntry(simpleRouter, generateEntry())
            addEntry(parallelRouter, generateEntry())
        }
        println("warm up took $time ms")
    }

    "add entries using map" {
        val time = measureTimeMillis {
            entries.forEach { addEntry(simpleRouter, it) }
        }
        println("sync takes $time ms to add")
    }

    "resolve time" {
        val resolveTime = measureTimeMillis {
            simpleRouter.resolve(testEntry.first)
        }
        println("sync resolve takes $resolveTime ms to resolve")
    }

    "add entries using parallel stream" {
        val time = measureTimeMillis {
            entries.parallelStream().forEach { addEntry(parallelRouter, it) }
        }
        println("parallel takes $time ms to add")
    }

    "resolve time parallel" {
        val resolveTime = measureTimeMillis {
            parallelRouter.resolve(testEntry.first)
        }
        println("parallel resolve takes $resolveTime ms to resolve")
    }
})

