package nolambda.linkrouter.processor

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.io.File

class RouteInitGeneratorSpec : StringSpec({
    "It should be generated" {
        val nodes = listOf(
            RouteInitNode(
                "ProductInit",
                "com.something",
                "String"
            )
        )

        @Suppress("BlockingMethodInNonBlockingContext")
        val temp = File.createTempFile("RouteInit", ".kt").parentFile

        val generator = RouteInitGenerator(temp.absolutePath, nodes)
        generator.generate()

        val dest = File("${temp.absoluteFile}/nolambda/init/route/StringRouteInit.kt")
        dest.exists() shouldBe true

        val text = dest.readText()

        text.contains("class StringRouteInit") shouldBe true
        text.contains("ProductInit::class.java.newInstance().onInit(appContext)") shouldBe true
        text.contains("import android.content.Context") shouldBe true
        text.contains("import com.something.ProductInit") shouldBe true

        dest.delete()
    }
})