package nolambda.linkrouter.processor

class FileGenerator(
    className: String,
    packageName: String,
    greeting: String = "Merry Christmas!!"
) {

    private val contentTemplate = """
        package $packageName
        class $className {
             fun greeting() = "$greeting"
        }
    """.trimIndent()

    fun getContent(): String {
        return contentTemplate
    }

}

