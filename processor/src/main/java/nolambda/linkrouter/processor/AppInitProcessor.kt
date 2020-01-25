package nolambda.linkrouter.processor

import com.google.auto.service.AutoService
import nolambda.linkrouter.annotations.AppInit
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
class AppInitProcessor : AbstractProcessor() {

    companion object {
        private const val ANDROID_MANIFEST_XML = "AndroidManifest.xml"
        private const val OPTION_ANDROID_MANIFEST = "AndroidManifestPath"
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(AppInitProcessor::class.java.name)
    }

    override fun process(
        p0: MutableSet<out TypeElement>?,
        env: RoundEnvironment
    ): Boolean {
        val providers = mutableListOf<ProviderNode>()

        env.getElementsAnnotatedWith(AppInit::class.java).forEach { el ->
            if (el.kind != ElementKind.CLASS || el.kind != ElementKind.METHOD) {
                processingEnv.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Annotation can only be applied to content provider or method"
                )
                return false
            }

            val className = el.simpleName.toString()
            val pack = processingEnv.elementUtils.getPackageOf(el).toString()

            providers.add(
                ProviderNode(className, pack)
            )
        }
        generatedXml(providers)

        return true
    }

    private fun generatedXml(providers: List<ProviderNode>) {
        val manifestPath = processingEnv.options[OPTION_ANDROID_MANIFEST]

        val output = File(manifestPath, ANDROID_MANIFEST_XML)
        val input = File(manifestPath, ANDROID_MANIFEST_XML)
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