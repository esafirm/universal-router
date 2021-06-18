package nolambda.linkrouter.android

import io.kotest.core.spec.style.StringSpec

class ObjectSpec : StringSpec({
    println("Instance 1: ${A::class.objectInstance}")
    println("Instance 2: ${A::class.objectInstance}")
})

private object A