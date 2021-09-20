package nolambda.linkrouter

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import nolambda.linkrouter.DeepLinkUri.Companion.toDeepLinkUri
import nolambda.linkrouter.matcher.DeepLinkEntryMatcher
import nolambda.linkrouter.matcher.UriMatcher

class UriRouterSpec : StringSpec({

    val routers = listOf(
        SimpleUriRouter<String>(),
        KeyUriRouter { "${it.scheme}${it.host}" }
    )

    "it should match and resolve to respected path" {
        routers.forEach { router ->
            val pathMap = mapOf(
                "http://test.com/promo" to "1",
                "http://test.com/promo-list" to "2",
                "http://test.com/promo-list/promo/" to "3",
                "http://test.com/promo-list/item1" to "4",
                "http://test.com/promo-list/item2" to "5",
                "http://test.com/promo-list/item3" to "6",
                "http://test.com/promo-list/item4" to "7",
                "http://test.com/promo-list/{a}" to "8",
            )

            println("Using $router")

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
        }
    }

    "it should match normal regex" {
        routers.forEach { router ->
            router.addEntry("http://something.com/.*/{a}") { _, param -> param["a"].orEmpty() }
            val result = router.resolve("http://something.com/aaaa/true")

            result shouldBe "true"
        }
    }

    "it should ignore extra path in url" {
        routers.forEach { router ->
            router.addEntry("https://example.com/test/{page}/extra") { _, _ -> "" }
            val result = router.resolve("https://example.com/test/2")

            result shouldBe null
        }
    }

    "it should match url with extra query parameter" {
        routers.forEach { router ->
            val expectedResult = "hi!"
            router.addEntry("https://example.com/test/{page}") { _, _ -> expectedResult }
            val result = router.resolve("https://example.com/test/2?utm_source=facebook.com")

            result shouldBe expectedResult
        }
    }

    "it should match custom matcher" {
        routers.forEach { router ->
            val expectedResult = "123"
            router.addEntry("https://test.com?show=true", matcher = object : UriMatcher {
                override fun match(entry: DeepLinkEntry, url: String): Boolean {
                    return DeepLinkEntryMatcher.match(entry, url) && url.toDeepLinkUri()
                        .queryParameter("show") == "true"
                }
            }) { _, _ -> expectedResult }


            router.resolve("https://test.com?show=false") shouldBe null
            router.resolve("https://test.com?show=true") shouldBe expectedResult
        }
    }
})