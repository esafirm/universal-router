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
            val routeClass = it.routeClass
            val identifier = routeClass.substring(routeClass.lastIndexOf(".") + 1)
            val name = "${identifier}$FILE_SUFFIX"
            val file = File(realDest, name)
            file.writeText(createClass(packageName, it, identifier, routeClass))
        }
    }

    private fun createClass(
        packageName: String,
        node: RouteInitNode,
        routeIdentifier: String,
        routeClass: String
    ): String {
        return """
        package $packageName
        
        import android.content.Context
        import ${node.packageName}.${node.className}
        
        class ${routeIdentifier}RouteInit(private val appContext: Context) {
             init {
                ${node.className}::class.java.newInstance().onInit(appContext)
             }
        }
        """.trimIndent()
    }

}

