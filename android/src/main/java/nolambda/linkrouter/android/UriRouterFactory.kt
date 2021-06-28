package nolambda.linkrouter.android

import nolambda.linkrouter.KeyUriRouter
import nolambda.linkrouter.SimpleUriRouter
import nolambda.linkrouter.UriRouter

class KeyUriRouterFactory : UriRouterFactory {
    override fun create(): UriRouter<UriResult> {
        return KeyUriRouter(RouterPlugin.logger) { entry ->
            "${entry.uri.scheme}${entry.uri.host}"
        }
    }
}

class SimpleUriRouterFactory : UriRouterFactory {
    override fun create(): UriRouter<UriResult> {
        return SimpleUriRouter()
    }
}

interface UriRouterFactory {
    fun create(): UriRouter<UriResult>
}