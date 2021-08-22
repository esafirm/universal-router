package nolambda.linkrouter.android.extra.stack

import nolambda.linkrouter.android.BaseRoute
import java.util.*

class UUIDItemIdGenerator : ItemIdGenerator {
    override fun generate(route: BaseRoute<*>, param: Any?): String {
        return UUID.randomUUID().toString()
    }
}