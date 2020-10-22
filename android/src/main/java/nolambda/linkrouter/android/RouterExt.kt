package nolambda.linkrouter.android

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

inline fun <reified T> LifecycleOwner.addRouterProcessor(
    router: RouterComponents,
    noinline processor: RouteProcessor<T>
) {
    lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun onCreate() {
            router.addProcessor(T::class.java, processor)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            router.removeProcessor(processor)
        }
    })

    if (lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
        router.addProcessor(T::class.java, processor)
    }
}