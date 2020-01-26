package nolambda.linkrouter.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Navigate(
    val route: KClass<*>
)