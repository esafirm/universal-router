package nolambda.linkrouter.processor

import java.io.File

class RouteInitGenerator(
    private val destDirectory: String,
    private val routeInits: List<RouteInitNode>
) {

    companion object {
        private const val FILE_NAME = "RouteInit.kt"
        private const val PACKAGE = "nolambda/init/route"
    }

    fun generate() {
        val packageName = PACKAGE.replace("/", ".")
        val realDest = File("$destDirectory/$PACKAGE")

        if (!realDest.exists()) realDest.mkdirs()

        val file = File(realDest, FILE_NAME)
        file.writeText(createClass(packageName))
    }

    private fun createClass(packageName: String): String {
        val imports = routeInits.joinToString("\n") {
            "import ${it.packageName}.${it.className}"
        }

        val initScripts = routeInits.joinToString("\n") {
            "${it.className}::class.java.newInstance().onInit(appContext)"
        }

        return """
        package $packageName
        
        import android.content.Context
        $imports
        
        internal class RouteInit(private val appContext: Context) {
             init {
                $initScripts
             }
        }
        """.trimIndent()
    }

}

