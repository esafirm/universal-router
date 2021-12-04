package nolambda.linkrouter.android

import nolambda.linkrouter.DeepLinkUri.Companion.toDeepLinkUri
import nolambda.linkrouter.KeyUriRouter
import nolambda.linkrouter.SimpleUriRouter
import nolambda.linkrouter.UriRouter
import nolambda.linkrouter.UriRouterLogger
import java.util.concurrent.ConcurrentHashMap
import kotlin.system.measureTimeMillis

class KeyUriRouterFactory(
    private val logger: UriRouterLogger? = RouterPlugin.logger,
    private val keyExtractor: (String) -> String = { it ->
        val uri = it.toDeepLinkUri()
        "${uri.scheme}${uri.host}"
    }
) : UriRouterFactory {
    override fun create(): UriRouter<UriResult> {
        return KeyUriRouter(logger, keyExtractor)
    }
}

class SimpleUriRouterFactory(
    private val logger: UriRouterLogger? = RouterPlugin.logger,
    private val isSupportConcurrent: Boolean = false
) : UriRouterFactory {
    override fun create(): UriRouter<UriResult> {
        return SimpleUriRouter(
            logger = logger,
            dataHolder = if (isSupportConcurrent) {
                ConcurrentHashMap()
            } else {
                mutableMapOf()
            }
        )
    }
}

interface UriRouterFactory {
    fun create(): UriRouter<UriResult>
}