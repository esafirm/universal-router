package nolambda.linkrouter

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class StringRouter : UriRouter<String>() {

    init {
        addEntry("nolambda://test/{a}/{b}", "https://test/{a}/{b}") {
            val first = it["a"]
            val second = it["b"]
            "$second came to the wrong neighborhood $first"
        }
    }

    fun goTo(uri: String) {
        val text = resolve(uri)
        println(text)
    }
}

class StringRouterSpec : StringSpec({

    val stringRouter = StringRouter()

    "Should return and print valid text" {
        stringRouter.resolve("nolambda://test/bro/you") shouldBe "you came to the wrong neighborhood bro"
        stringRouter.resolve("https://test/bro/you") shouldBe "you came to the wrong neighborhood bro"
    }
})
