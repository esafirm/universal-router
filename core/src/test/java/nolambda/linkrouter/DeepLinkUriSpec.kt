package nolambda.linkrouter

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class DeepLinkUriSpec : StringSpec({

    "Should parse properly" {
        val deeplink = DeepLinkEntry.parse("http://something.js/{a}/{b}")
        val parameters = deeplink.getParameters("http://something.js/test/123")

        parameters.size shouldBe 2
        parameters["a"] shouldBe "test"
        parameters["b"] shouldBe "123"
    }

    "Should no crash on no parameters" {
        val deeplink = DeepLinkEntry.parse("nolamnda://test/{asd}")
        val parametes = deeplink.getParameters("nolambda://test")

        parametes.size shouldBe 0
    }

    "It should handle slash properly" {
        val deeplink = DeepLinkEntry.parse("nolambda://test/{a}/{b}")
        val parameters = deeplink.getParameters("nolambda://test/a/b?aaaa=1")

        parameters.size shouldBe 2
        parameters["a"] shouldBe "a"
        parameters["b"] shouldBe "b"
    }
})


