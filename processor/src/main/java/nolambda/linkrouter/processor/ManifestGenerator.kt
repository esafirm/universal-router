package nolambda.linkrouter.processor

import java.io.File

class ManifestGenerator(
    private val dest: String,
    private val writer: ManifestWriter
) {
    fun generate(): Boolean {
        val destFile = File(dest)
        destFile.writeText(writer.writeOut())
        return true
    }
}