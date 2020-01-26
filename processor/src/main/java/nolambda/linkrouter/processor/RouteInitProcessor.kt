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
import javax.lang.model.type.MirroredTypeException

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor::class)
class RouteInitProcessor : AbstractProcessor() {

    companion object {
        private const val OPTION_KAPT_KOTLIN_GENERATED = "kapt.kotlin.generated"
    }

    private val logger by lazy { Logger(processingEnv) }

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

            logger.note(
                "Note: ${el.enclosedElements.joinToString { it.simpleName }}"
            )

            val annotation = el.getAnnotation(Navigate::class.java)
            val pack = processingEnv.elementUtils.getPackageOf(el).toString()
            routeInits.add(
                RouteInitNode(el.simpleName.toString(), pack, getRouteName(annotation))
            )
        }

        if (routeInits.isEmpty().not()) {
            generateFiles(routeInits)
        }

        return true
    }

    private fun getRouteName(navigate: Navigate): String {
        return try {
            navigate.route.simpleName!!
        } catch (mte: MirroredTypeException) {
            val fullType = mte.typeMirror.toString()
            fullType.substring(fullType.lastIndexOf(".") + 1)
        }
    }

    private fun generateFiles(routeInits: List<RouteInitNode>) {
        val dest = processingEnv.options[OPTION_KAPT_KOTLIN_GENERATED]
            ?: throw IllegalStateException("Kapt option not exist")
        RouteInitGenerator(dest, routeInits).generate()
    }

}