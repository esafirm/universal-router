package nolambda.linkrouter

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class UriRouterSpec : StringSpec({

    val createTestRouter = { object : UriRouter<String>() {} }

    "it should match and resolve to respected path" {

        val router = createTestRouter()
        val pathMap = mapOf(
            "http://test.com/promo" to "1",
            "http://test.com/promo-list" to "2",
            "http://test.com/promo-list/promo" to "3",
            "http://test.com/promo-list/{a}" to "4",
        )

        pathMap.keys.forEach { key ->
            router.addEntry(key) { _, param ->
                println("Param: $param")
                pathMap[key] ?: error("")
            }
        }

        pathMap.keys.forEach { key ->
            val resolved = router.resolve(key)

            println("Expected -> $key : ${pathMap[key]}")
            println("Actual -> $key : $resolved")
            println("------")

            resolved shouldBe pathMap[key]
        }

        // The order is important
        // if we register this after the path above the path will resolved to the previous path
        router.addEntry("http://test.com/promo-list/cap") { _, _ -> "5" }
        router.resolve("http://test.com/promo-list/cap") shouldBe "4"
    }

    "it should match normal regex" {
        val router = createTestRouter()
        router.addEntry("http://something.com/.*/{a}") { _, param -> param["a"].orEmpty() }
        val result = router.resolve("http://something.com/aaaa/true")

        result shouldBe "true"
    }
})