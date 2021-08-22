@file:Suppress("NON_EXHAUSTIVE_WHEN")

package nolambda.linkrouter.android

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

inline fun <reified T> LifecycleOwner.addRouterProcessor(
    router: RouterComponents,
    noinline processor: RouteProcessor<T>
) {
    lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            when (event) {
                Lifecycle.Event.ON_CREATE -> router.addTypeProcessor(processor)
                Lifecycle.Event.ON_DESTROY -> router.removeProcessor(processor)
            }
        }
    })
}