package nolambda.linkrouter.processor

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.mockk.every
import io.mockk.mockk
import java.io.File

class ManifestGeneratorSpec : StringSpec({
    val mockWriter = mockk<ManifestWriter> {
        every { writeOut() } returns "Something"
    }
    val tempFile = File.createTempFile("manifest", ".xml")

    val generator = ManifestGenerator(tempFile.absolutePath, mockWriter)

    generator.generate() shouldBe true
    tempFile.readText() shouldBe "Something"
})