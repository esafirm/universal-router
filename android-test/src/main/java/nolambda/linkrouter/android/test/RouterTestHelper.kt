package nolambda.linkrouter.android.test

import nolambda.linkrouter.android.AbstractAppRouter
import nolambda.linkrouter.android.BaseRoute

fun AbstractAppRouter<*>.testHit(route: BaseRoute<*>): Boolean {
    var isHit = false
    register(route) {
        isHit = true
    }
    push(route)
    return isHit
}