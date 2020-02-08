package nolambda.linkrouter.android

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

inline fun <reified T> LifecycleOwner.addRouterProcessor(
    noinline processor: RouteProcessor<T>
) {
    lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun onCreate() {
            Router.addProcessor(processor)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            Router.removeProcessor(processor)
        }
    })

    if (lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
        Router.addProcessor(processor)
    }
}