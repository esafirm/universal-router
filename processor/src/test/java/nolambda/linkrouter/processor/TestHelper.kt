package nolambda.linkrouter.processor

import java.nio.file.Paths

object TestHelper {
    fun getResourcePath(fileName: String): String {
        val res = javaClass.classLoader.getResource(fileName)
            ?: throw IllegalStateException("No file found")
        val file = Paths.get(res.toURI()).toFile()
        return file.absolutePath
    }
}
