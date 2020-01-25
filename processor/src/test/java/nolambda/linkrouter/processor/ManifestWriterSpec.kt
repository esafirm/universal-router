package nolambda.linkrouter.processor;

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.io.File

class ManifestWriterSpec : StringSpec({

    val providers = listOf(
        ProviderNode("nolambda.sample.RouteInit", "sample"),
        ProviderNode("nolambda.sample.SecondRouteInit", "sample2")
    )

    val file = File(TestHelper.getResourcePath("manifest.xml"))

    val writer = ManifestWriter(file, providers)

    val result = writer.writeOut()

    result.contains("nolambda.sample.RouteInit") shouldBe true
    result.contains("nolambda.sample.SecondRouteInit") shouldBe true

    println("Result: $result")
})