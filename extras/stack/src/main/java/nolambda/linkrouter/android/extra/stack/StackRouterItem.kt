package nolambda.linkrouter.android.extra.stack

import nolambda.linkrouter.android.BaseRoute

data class StackRouterItem(
    val id: String,
    val route: BaseRoute<*>,
    val param: Any?
)