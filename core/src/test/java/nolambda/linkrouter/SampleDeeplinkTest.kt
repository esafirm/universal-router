package nolambda.linkrouter

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class StringRouterSpec : StringSpec({

    val stringRouter = SimpleUriRouter<String>()

    stringRouter.addEntry("nolambda://test/{a}/{b}", "https://test/{a}/{b}") { _, param ->
        val first = param["a"]
        val second = param["b"]
        "$second came to the wrong neighborhood $first"
    }

    stringRouter.addEntry("http://test.com/{a}") { uri, param ->
        val trackId = uri.queryParameter("trackId")
        "Track ID for ${param["a"]} is $trackId"
    }

    "should return and print valid text" {
        stringRouter.resolve("nolambda://test/bro/you") shouldBe "you came to the wrong neighborhood bro"
        stringRouter.resolve("https://test/bro/you") shouldBe "you came to the wrong neighborhood bro"
    }

    "it should return path and query" {
        stringRouter.resolve("http://test.com/featureone?trackId=123") shouldBe "Track ID for featureone is 123"
    }
})
