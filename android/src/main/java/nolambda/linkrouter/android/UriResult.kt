package nolambda.linkrouter.android

import nolambda.linkrouter.DeepLinkUri

/**
 * URI Router resolved data
 */
data class UriResult(
    val uri: DeepLinkUri,
    val route: BaseRoute<*>,
    val param: Map<String, String>
)
