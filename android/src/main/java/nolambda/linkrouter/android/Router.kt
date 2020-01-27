@file:Suppress("UNCHECKED_CAST")

package nolambda.linkrouter.android

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import nolambda.linkrouter.SimpleRouter
import nolambda.linkrouter.UriRouter
import nolambda.linkrouter.addEntry

typealias RouteHandler<P, R> = (RouteResult<P>) -> R
typealias RouteProcessor<T> = (T) -> Unit

class RouteResult<T>(
    val param: T? = null,
    val rawParam: Map<String, String>
)

object Router {
    private object AndroidSimpleRouter : SimpleRouter<RouteHandler<*, *>>()
    private object AndroidUriRouter : UriRouter<Unit>()

    private val simpleRouter = AndroidSimpleRouter
    private val uriRouter = AndroidUriRouter

    private val autoRegister by lazy { RouteAutoRegister(RouterPlugin) }

    private val EMPTY_PARAMETER = emptyMap<String, String>()
    private val EMPTY_RESULT = RouteResult<Unit>(rawParam = EMPTY_PARAMETER)

    private val processors = linkedSetOf<Pair<Class<*>, RouteProcessor<in Any>>>()

    fun cleanRouter() {
        simpleRouter.clear()
        uriRouter.clear()
        processors.clear()
    }

    fun removeProcessor(processor: RouteProcessor<*>) {
        processors.removeAll { it.second == processor }
    }

    fun <T> addProcessor(clazz: Class<T>, processor: RouteProcessor<T>) {
        processors.add(clazz to processor as RouteProcessor<in Any>)
    }

    inline fun <reified T> addProcessor(noinline processor: RouteProcessor<T>) {
        addProcessor(T::class.java, processor)
    }

    inline fun <reified T> addProcessorWithLifecycle(
        lifecycleOwner: LifecycleOwner,
        noinline processor: RouteProcessor<T>
    ) {
        val lifecycle = lifecycleOwner.lifecycle
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() {
                addProcessor(processor)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                removeProcessor(processor)
            }
        })

        if (lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            addProcessor(processor)
        }
    }

    fun <P, R> register(route: BaseRoute<P>, handler: RouteHandler<P, R>) {
        val paths = route.routePaths

        // Handle non path
        simpleRouter.addEntry(route) {
            @Suppress("UNCHECKED_CAST")
            handler as RouteHandler<*, *>
        }

        // Handle the path
        if (paths.isEmpty()) return

        paths.forEach { path ->
            if (path.isNotBlank()) {
                uriRouter.addEntry(path) {
                    val result = if (route is RouteWithParam<P> && route.paramMapper != null) {
                        handler.invoke(
                            RouteResult(
                                param = route.paramMapper.invoke(it),
                                rawParam = it
                            )
                        )
                    } else {
                        handler.invoke(RouteResult(rawParam = it))
                    }
                    invokeProcessor(result)
                }
            }
        }
    }

    fun <P> push(route: BaseRoute<P>) {
        autoRegister.registerScreenIfNeeded(route)
        invokeProcessor(
            simpleRouter.resolve(route).invoke(EMPTY_RESULT)
        )
    }

    fun <P> push(route: RouteWithParam<P>, param: P) {
        autoRegister.registerScreenIfNeeded(route)
        invokeProcessor(
            simpleRouter.resolve(route).invoke(
                RouteResult(
                    param = param,
                    rawParam = EMPTY_PARAMETER
                )
            )
        )
    }

    fun goTo(uri: String) {
        uriRouter.resolve(uri)
    }

    private fun invokeProcessor(result: Any?) {
        if (result == null) return
        if (processors.isEmpty()) return

        val clazz = result.javaClass
        processors.forEach { (c, processor) ->
            if (c.isAssignableFrom(clazz)) {
                processor.invoke(result)
            }
        }
    }
}