package nolambda.linkrouter.android

import io.kotlintest.specs.StringSpec

class ObjectSpec : StringSpec({
    println("Instance 1: ${A::class.objectInstance}")
    println("Instance 2: ${A::class.objectInstance}")
})

object A