package nolambda.linkrouter

import io.kotest.core.spec.style.StringSpec
import nolambda.linkrouter.DeepLinkUri.Companion.toDeepLinkUri
import java.util.*
import kotlin.system.measureTimeMillis

class PerformanceTest : StringSpec({

    val size = 20_000L
    val random = Random(size)

    val logger = { log: String -> println(log) }

    val simpleRouter = SimpleUriRouter<Unit>(logger)
    val keyRouter = KeyUriRouter<Unit>(logger) {
        val deepLinkUri = it.toDeepLinkUri()
        "${deepLinkUri.scheme}${deepLinkUri.host}${deepLinkUri.pathSegments.size}"
    }

    val generateEntry = {
        val domain = random.nextInt().toString()
        val simpleHttp = "http://${domain}.js/{kupon}/{customer_id}"
        val simpleHttps = "https://${domain}.js/{kupon}/{customer_id}"
        val constantDomain = "http://test.com/${domain}"
        arrayOf(simpleHttp, simpleHttps, constantDomain)
    }

    val addEntry = { router: UriRouter<Unit>, entries: Array<String> ->
        router.addEntry(*entries) { _, _ ->
            print("Resolved!")
        }
    }

    val entries = (0 until size).map { generateEntry() }
    val testEntry = entries[entries.size / 2]

    "warm up" {
        val time = measureTimeMillis {
            addEntry(simpleRouter, generateEntry())
            addEntry(keyRouter, generateEntry())
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
            simpleRouter.resolve(testEntry.first())
        }
        println("sync resolve takes $resolveTime ms to resolve")
    }

    "add entries using container" {
        val time = measureTimeMillis {
            entries.forEach { addEntry(keyRouter, it) }
        }
        println("container takes $time ms to add")
    }

    "resolve time container" {
        val resolveTime = measureTimeMillis {
            keyRouter.resolve(testEntry.first())
        }
        println("container resolve takes $resolveTime ms to resolve")
    }
})

