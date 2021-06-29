package nolambda.linkrouter.android

import nolambda.linkrouter.DeepLinkEntry
import nolambda.linkrouter.KeyUriRouter
import nolambda.linkrouter.SimpleUriRouter
import nolambda.linkrouter.UriRouter
import nolambda.linkrouter.UriRouterLogger

class KeyUriRouterFactory(
    private val logger: UriRouterLogger? = RouterPlugin.logger,
    private val keyExtractor: (DeepLinkEntry) -> String = { entry -> "${entry.uri.scheme}${entry.uri.host}" }
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