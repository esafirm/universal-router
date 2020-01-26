package nolambda.linkrouter.processor

import java.io.File

class RouteInitGenerator(
    private val destDirectory: String,
    private val routeInits: List<RouteInitNode>
) {

    companion object {
        private const val FILE_SUFFIX = "RouteInit.kt"
        private const val PACKAGE = "nolambda/init/route"
    }

    fun generate() {
        val packageName = PACKAGE.replace("/", ".")
        val realDest = File("$destDirectory/$PACKAGE")

        if (!realDest.exists()) realDest.mkdirs()

        routeInits.forEach {
            val name = "${it.routeName}$FILE_SUFFIX"
            val file = File(realDest, name)
            file.writeText(createClass(packageName, it, it.routeName))
        }
    }

    private fun createClass(
        packageName: String,
        node: RouteInitNode,
        identifier: String
    ): String {
        return """
        package $packageName
        
        import android.content.Context
        import ${node.packageName}.${node.className}
        
        class ${identifier}RouteInit(private val appContext: Context) {
             init {
                ${node.className}::class.java.newInstance().onInit(appContext)
             }
        }
        """.trimIndent()
    }

}

