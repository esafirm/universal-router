package nolambda.linkrouter.android.extra.stack

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import nolambda.linkrouter.android.Route

class UUIDItemGeneratorSpec : StringSpec({

    "it should generate id" {
        val idGenerator = UUIDItemIdGenerator()

        val sampleRoute = object : Route() {}

        val item1 = idGenerator.generate(sampleRoute, "123")
        val item2 = idGenerator.generate(sampleRoute, null)

        item1.isNotBlank() shouldBe true
        item2.isNotBlank() shouldBe true
        item1 shouldNotBe item2
    }
})