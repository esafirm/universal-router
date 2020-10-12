package nolambda.linkrouter

open class RoutePath(
    private vararg val basePaths: String
) {
    fun paths(vararg items: String): Array<String> {
        val paths = mutableListOf<String>()
        basePaths.forEach { base ->
            items.map { item ->
                paths.add(base + item)
            }
        }
        return paths.toTypedArray()
    }

    operator fun plus(path: RoutePath): RoutePath {
        val combinedBasePaths = arrayOf(*this.basePaths, *path.basePaths)
        return RoutePath(*combinedBasePaths)
    }

    fun wrap(path: RoutePath): RoutePath {
        val wrappedBasePath = paths(*path.basePaths)
        return RoutePath(*wrappedBasePath)
    }
}