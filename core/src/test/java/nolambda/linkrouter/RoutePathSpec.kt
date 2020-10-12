package nolambda.linkrouter

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class RoutePathSpec : StringSpec({

    "it should provide simple path" {
        val path = RoutePath("app://test.com", "https://facebook.com")
        val result = path.paths("/cart")

        result shouldBe arrayOf("app://test.com/cart", "https://facebook.com/cart")
    }

    "it should combine base paths" {
        val firstPath = RoutePath("app://test.com")
        val secondPath = RoutePath("https://facebook.com")
        val combinedPath = firstPath + secondPath

        val result = combinedPath.paths("/cart/{id}")

        result shouldBe arrayOf("app://test.com/cart/{id}", "https://facebook.com/cart/{id}")
    }

    "it should wrap the route" {
        val webProtocol = RoutePath("http://", "https://")
        val webLink = webProtocol.wrap(RoutePath(
            "m.bukatoko.com",
            "www.bukatoko.com",
        ))

        val appProtocol = RoutePath("app://")
        val appLink = appProtocol.wrap(RoutePath(
            "bukatoko",
        ))

        val allLink = webLink + appLink
        val result = allLink.paths("/cart")

        result.size shouldBe 5
        result shouldBe arrayOf(
            "http://m.bukatoko.com/cart",
            "http://www.bukatoko.com/cart",
            "https://m.bukatoko.com/cart",
            "https://www.bukatoko.com/cart",
            "app://bukatoko/cart"
        )
    }
})