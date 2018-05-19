package nolambda.linkrouter

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

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
})


