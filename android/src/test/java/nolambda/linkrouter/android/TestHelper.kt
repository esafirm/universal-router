package nolambda.linkrouter.android

fun AbstractAppRouter<*>.testHit(route: BaseRoute<*>): Boolean {
    var isHit = false
    register(route) {
        isHit = true
    }
    push(route)
    return isHit
}