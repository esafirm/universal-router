package nolambda.linkrouter.android

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import nolambda.linkrouter.DeepLinkUri
import nolambda.linkrouter.android.test.DeepLinkAssertions
import nolambda.linkrouter.android.test.testDeepLink

class DeepLinkSpec : StringSpec({

    data class IdParam(val id: String)

    val userRoute = object : RouteWithParam<IdParam>("nolambda://user/{id}") {
        override fun mapUri(uri: DeepLinkUri, raw: Map<String, String>): IdParam {
            return IdParam(raw["id"] ?: error(""))
        }
    }

    val itemRoute = object : RouteWithParam<IdParam>("https://nolambda.stream/items/{id}") {
        override fun mapUri(uri: DeepLinkUri, raw: Map<String, String>): IdParam {
            return IdParam(raw["id"] ?: error(""))
        }
    }

    val router = object : AbstractAppRouter<Unit>() {}

    "test deep link" {
        router.testDeepLink(listOf(
            userRoute to listOf(
                "nolambda://user/1" to DeepLinkAssertions.shouldValid(),
            ),
            itemRoute to listOf(
                "https://nolambda.stream/items/10" to { it.routeParam?.param?.id shouldBe "10" },
                "http://nolambda.stream/items/10" to DeepLinkAssertions.shouldValid(false)
            )
        ))
    }
})