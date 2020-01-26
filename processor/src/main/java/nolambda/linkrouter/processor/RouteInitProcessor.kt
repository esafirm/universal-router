package nolambda.linkrouter.processor

import com.google.auto.service.AutoService
import nolambda.linkrouter.annotations.Navigate
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor::class)
class RouteInitProcessor : AbstractProcessor() {

    companion object {
        private const val OPTION_KAPT_KOTLIN_GENERATED = "kapt.kotlin.generated"
    }

    private val logger = Logger(processingEnv)

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(Navigate::class.java.name)
    }

    override fun process(
        p0: MutableSet<out TypeElement>?,
        env: RoundEnvironment
    ): Boolean {
        val routeInits = mutableListOf<RouteInitNode>()

        env.getElementsAnnotatedWith(Navigate::class.java).forEach { el ->
            if (el.kind != ElementKind.CLASS) {
                logger.error(
                    "Annotation can only be applied to content provider or method"
                )
                return false
            }

            val pack = processingEnv.elementUtils.getPackageOf(el).toString()
            routeInits.add(
                RouteInitNode(el.simpleName.toString(), pack)
            )
        }

        if (routeInits.isEmpty().not()) {
            generateFiles(routeInits)
        }

        return true
    }

    private fun generateFiles(routeInits: List<RouteInitNode>) {
        logger.error("Generate: ${routeInits.size}")

        val dest = processingEnv.options[OPTION_KAPT_KOTLIN_GENERATED]
            ?: throw IllegalStateException("Kapt option not exist")
        RouteInitGenerator(dest, routeInits).generate()
    }

}