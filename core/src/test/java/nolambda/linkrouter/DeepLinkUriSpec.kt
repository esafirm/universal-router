package nolambda.linkrouter

import io.kotest.assertions.throwables.shouldThrow
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

    "It should handle match properly" {
        val deeplink = DeepLinkEntry.parse("https://example.com")

        deeplink.matches("https://example.com") shouldBe true
        deeplink.matches("https://example1com") shouldBe false
        deeplink.matches("https://example.com?test-param=true") shouldBe true
    }

    "Regex should be functioning in path" {
        val deeplink = DeepLinkEntry.parse("https://test.com/.*")
        deeplink.matches("https://test.com/asdasdasd/asdasdsad/asdasd") shouldBe true
    }

    "Regex should not functioning in scheme and host" {
        shouldThrow<IllegalArgumentException> {
            DeepLinkEntry.parse(".*://test.com/")
        }
        shouldThrow<IllegalArgumentException> {
            DeepLinkEntry.parse("https://[a|b].com/")
        }
    }
})


