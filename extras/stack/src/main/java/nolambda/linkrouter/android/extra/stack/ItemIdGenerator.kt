package nolambda.linkrouter.android.extra.stack

import nolambda.linkrouter.android.BaseRoute

interface ItemIdGenerator {
    fun generate(route: BaseRoute<*>, param: Any?): String
}