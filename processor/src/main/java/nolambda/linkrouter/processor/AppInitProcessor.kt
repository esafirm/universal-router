package nolambda.linkrouter.processor

import com.google.auto.service.AutoService
import nolambda.linkrouter.annotations.AppInit
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor::class)
class AppInitProcessor : AbstractProcessor() {

    companion object {
        private const val ANDROID_MANIFEST_XML = "AndroidManifest.xml"
        private const val OPTION_ANDROID_MANIFEST = "AndroidManifestPath"
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(AppInit::class.java.name)
    }

    override fun process(
        p0: MutableSet<out TypeElement>?,
        env: RoundEnvironment
    ): Boolean {
        val providers = mutableListOf<ProviderNode>()

        env.getElementsAnnotatedWith(AppInit::class.java).forEach { el ->
            if (el.kind != ElementKind.CLASS) {
                printError(
                    "Annotation can only be applied to content provider or method"
                )
                return false
            }

            val pack = processingEnv.elementUtils.getPackageOf(el).toString()
            val className = "${pack}.${el.simpleName}"

            providers.add(
                ProviderNode(className, pack)
            )
        }
        generatedXml(providers)

        return true
    }

    private fun printError(error: String) {
        processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, error)
    }

    private fun printWarning(warning: String) {
        processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, warning)
    }

    private fun generatedXml(providers: List<ProviderNode>) {
        val manifestPath = processingEnv.options[OPTION_ANDROID_MANIFEST]!!
        val input = File(manifestPath)
        val dir = input.parentFile.absolutePath

        providers.forEach {
            printWarning(
                """
                Provider: ${it.className}
            """.trimIndent()
            )
        }

        val output = File(dir, ANDROID_MANIFEST_XML)
        val writer = ManifestWriter(
            input.absoluteFile,
            providers
        )

        val generator = ManifestGenerator(
            output.absolutePath,
            writer
        )

        generator.generate()
    }
}