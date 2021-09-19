package nolambda.linkrouter.android

import nolambda.linkrouter.DeepLinkUri
import nolambda.linkrouter.KeyUriRouter
import nolambda.linkrouter.SimpleUriRouter
import nolambda.linkrouter.UriRouter
import nolambda.linkrouter.UriRouterLogger

class KeyUriRouterFactory(
    private val logger: UriRouterLogger? = RouterPlugin.logger,
    private val keyExtractor: (DeepLinkUri) -> String = { uri -> "${uri.scheme}${uri.host}" }
) : UriRouterFactory {
    override fun create(): UriRouter<UriResult> {
        return KeyUriRouter(logger, keyExtractor)
    }
}

class SimpleUriRouterFactory(
    private val logger: UriRouterLogger? = RouterPlugin.logger
) : UriRouterFactory {
    override fun create(): UriRouter<UriResult> {
        return SimpleUriRouter(logger)
    }
}

interface UriRouterFactory {
    fun create(): UriRouter<UriResult>
}