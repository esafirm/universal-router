package nolambda.linkrouter

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class StringRouter : Router<String>() {

    init {
        addEntry("nolambda://test/{a}/{b}", "https://test/{a}/{b}") {
            val first = it["a"]
            val second = it["b"]
            "$second came to the wrong neighborhood $first"
        }
    }

    override fun goTo(uri: String) {
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
